
(ns server-extensions.storage.engine
    (:require [mid-fruits.vector :as vector]
              [mongo-db.api      :as mongo-db]
              [prototypes.api    :as prototypes]
              [mid-extensions.storage.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.storage.engine
(def ROOT-DIRECTORY-ID    engine/ROOT-DIRECTORY-ID)
(def SAMPLE-FILE-ID       engine/SAMPLE-FILE-ID)
(def SAMPLE-FILE-FILENAME engine/SAMPLE-FILE-FILENAME)



;; -- Attach/detach item functions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn attach-media-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id item-id]
  (mongo-db/apply-document! "storage" directory-id #(update % :media/items vector/conj-item {:media/id item-id})
                            {:prototype-f #(prototypes/updated-document-prototype request :media %)}))

(defn detach-media-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id item-id]
  (mongo-db/apply-document! "storage" directory-id #(update % :media/items vector/remove-item {:media/id item-id})
                            {:prototype-f #(prototypes/updated-document-prototype request :media %)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-media-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) media-item
  ;
  ; @return (namespaced map)
  [{:keys [request]} media-item]
  (mongo-db/insert-document! "storage" media-item
                             {:prototype-f #(prototypes/added-document-prototype request :media %)}))
