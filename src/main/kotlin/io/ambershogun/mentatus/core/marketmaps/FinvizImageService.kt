package io.ambershogun.mentatus.core.marketmaps

import io.ambershogun.mentatus.core.marketmaps.entity.FinvizImageHolder
import io.ambershogun.mentatus.core.marketmaps.repo.FinvizImageHolderRepository
import org.springframework.stereotype.Service

@Service
class FinvizImageService(
        val finvizImageHolderRepository: FinvizImageHolderRepository
) {

    fun saveImageUrls(sectors: String, regions: String) {
        val holder = finvizImageHolderRepository.findById(HOLDER_ID)
                .orElse(FinvizImageHolder().apply { id = HOLDER_ID })

        holder.sectorUrl = sectors
        holder.regionsUrl = regions

        finvizImageHolderRepository.save(holder)
    }

    fun getHolder(): FinvizImageHolder {
        return finvizImageHolderRepository.findById(HOLDER_ID)
                .orElse(FinvizImageHolder().apply { id = HOLDER_ID })
    }

    companion object {
        const val HOLDER_ID = "1"
    }
}