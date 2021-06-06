package com.fiora.note2.dao;
import com.fiora.note2.model.NetDisk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NetDiskESRepository extends ElasticsearchRepository<NetDisk, Integer>{
    Page<NetDisk> findByNameLikeOrPathLike(String name, String path, Pageable pageable);

    Page<NetDisk> findByNameLike(String name, Pageable pageable);
}
