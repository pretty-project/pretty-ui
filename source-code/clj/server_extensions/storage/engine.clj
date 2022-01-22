
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
  ; @param (map)(opt) attach-props
  ;  {:content-size (B)(opt)}
  ;
  ; @return (namespaced map)
  ([env directory-id item-id]
   (attach-media-item! env directory-id item-id {}))

  ([{:keys [request]} directory-id item-id {:keys [content-size]}]
   (letfn [(prototype-f [document] (prototypes/updated-document-prototype request :media document))
           (attach-f    [document] (cond-> document content-size (update :media/content-size + content-size)
                                                    :attach-item (update :media/items vector/conj-item {:media/id item-id})))]
          (mongo-db/apply-document! "storage" directory-id attach-f {:prototype-f prototype-f}))))

(defn detach-media-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (string) item-id
  ; @param (map)(opt) detach-props
  ;  {:content-size (B)(opt)}
  ;
  ; @return (namespaced map)
  ([env directory-id item-id]
   (detach-media-item! env directory-id item-id {}))

  ([{:keys [request]} directory-id item-id {:keys [content-size]}]
   (letfn [(prototype-f [document] (prototypes/updated-document-prototype request :media document))
           (detach-f    [document] (cond-> document content-size (update :media/content-size - content-size)
                                                    :detach-item (update :media/items vector/remove-item {:media/id item-id})))]
          (mongo-db/apply-document! "storage" directory-id detach-f {:prototype-f prototype-f}))))



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
