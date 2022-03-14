
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.media-browser.prototypes
    (:require [mid-fruits.vector                :as vector]
              [mongo-db.api                     :as mongo-db]
              [server-extensions.storage.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicated-directory-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props} document]
  (letfn [(f2 [{:media/keys [id] :as %}] (if (= id parent-id) {:media/id destination-id} %))
          (f1 [%]                        (vector/->items % f2))]
         (as-> document % (mongo-db/duplicated-document-prototype request :media %)
                          (if (= destination-id parent-id) % (update % :media/path f1)))))

(defn duplicated-file-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props} document]
  (letfn [(f3 [{:media/keys [id filename] :as %}] (assoc % :media/filename (engine/file-id->filename id filename)))
          (f2 [{:media/keys [id]          :as %}] (if (= id parent-id) {:media/id destination-id} %))
          (f1 [%]                                 (vector/->items % f2))]
         (as-> document % (mongo-db/duplicated-document-prototype request :media %)
                          (if (= destination-id parent-id) % (update % :media/path f1))
                          (f3 %))))
