package vttp2022.miniproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.repositories.AssetsRepository;

@Service
public class AssetsService {

    @Autowired
    private AssetsRepository assetsRepo;

    public List<Stock> getAssets(Integer userId) {
        return assetsRepo.findAssetsByUserId(userId);
    }

}
