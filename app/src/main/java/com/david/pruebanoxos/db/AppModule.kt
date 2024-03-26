package com.david.pruebanoxos.db

import android.app.Application
import androidx.room.Room
import com.david.pruebanoxos.activities.viewmodels.MainViewModel
import com.david.pruebanoxos.db.dao.TransaccionDao
import com.david.pruebanoxos.retrofit.api.PaymentApi
import com.david.pruebanoxos.retrofit.api.PaymentRepository
import com.david.pruebanoxos.retrofit.api.Repository
import com.david.pruebanoxos.retrofit.usecases.AnnulationUseCase
import com.david.pruebanoxos.retrofit.usecases.AuthorizationUseCase
import com.david.pruebanoxos.retrofit.usecases.TransactionInsertUseCase
import com.david.pruebanoxos.retrofit.usecases.TransactionUpdateUseCase
import com.david.pruebanoxos.retrofit.usecases.TransactionsGetAllUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DATABASE_NAME = "credibanco_database"

    /**
     * Provee la instancia del base de datos
     */
    @Singleton
    @Provides
    fun provideRoom( context: Application) =
        synchronized(this){
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }

    /**
     * Provee la instancia del base de datos
     */
    @Singleton
    @Provides
    fun provideAppDao(db: AppDatabase) = db.transactionDao

    /**
     * Provee la instancia del api
     */
    @Provides
    @Singleton
    fun provideCeibaApi(): PaymentApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.3.38:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentApi::class.java)
    }

    /**
     * Provee la instancia del repositorio
     */
    @Provides
    @Singleton
    fun provideRepository(transactionApi: PaymentApi, appDao: TransaccionDao): Repository {
        return PaymentRepository(transactionApi, appDao)
    }

    /**
     * Provee la instancia del caso de uso de las Autorizacciones
     */
    @Provides
    @Singleton
    fun provideAuthorizationUseCase(repository: Repository): AuthorizationUseCase {
        return AuthorizationUseCase(repository)
    }

    /**
     * Provee la instancia del caso de uso de las Anulaciones
     */
    @Provides
    @Singleton
    fun provideAnnulationUseCase(repository: Repository): AnnulationUseCase {
        return AnnulationUseCase(repository)
    }

    /**
     * Provee la instancia del caso de uso de las transacciones
     */
    @Provides
    fun provideTransactionViewModel(
        authorizationUseCase: AuthorizationUseCase,
        annulationUseCase: AnnulationUseCase,
        transactionInsertUseCase: TransactionInsertUseCase,
        transactionsUseCase: TransactionsGetAllUseCase,
        transactionUpdateUseCase: TransactionUpdateUseCase
    ): MainViewModel {
        return MainViewModel(authorizationUseCase, annulationUseCase,
            transactionInsertUseCase, transactionsUseCase, transactionUpdateUseCase)
    }
}