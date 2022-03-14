
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->files-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:params (map)
  ;    {:query (string)}}
  ;
  ; @example
  ;  (request->files-data {:params {"0" {:tempfile #object[java.io.File 0x4571e67a "/my-tempfile.tmp"}
  ;                                 "1" {:tempfile #object[java.io.File 0x4571e67a "/your-tempfile.tmp"}
  ;                                 :query [...]}})
  ;  =>
  ;  {"0" {:tempfile "/my-tempfile.tmp"}
  ;   "1" {:tempfile "/your-tempfile.tmp"}}
  ;
  ; @return (map)
  [{:keys [params]}]
  (letfn [(f [files-data dex file-data] (assoc files-data dex (update file-data :tempfile str)))]
         (reduce-kv f {} (dissoc params :query))))

(defn request->content-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [params]}]
  (letfn [(f [content-size _ {:keys [size]}] (+ content-size size))]
         (reduce-kv f 0 (dissoc params :query))))
