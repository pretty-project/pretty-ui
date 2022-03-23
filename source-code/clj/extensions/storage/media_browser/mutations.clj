
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-browser.mutations
    (:require [com.wsscode.pathom3.connect.operation       :as pathom.co :refer [defmutation]]
              [extensions.storage.core.helpers             :as core.helpers]
              [extensions.storage.media-browser.config     :as media-browser.config]
              [extensions.storage.media-browser.prototypes :as media-browser.prototypes]
              [extensions.storage.core.side-effects        :as core.side-effects]
              [mid-fruits.candy                            :refer [return]]
              [mid-fruits.time                             :as time]
              [mid-fruits.vector                           :as vector]
              [mongo-db.api                                :as mongo-db]
              [pathom.api                                  :as pathom]
              [plugins.item-browser.api                    :as item-browser]
              [server-fruits.io                            :as io]))



;; -- Permanently delete item(s) functions ------------------------------------
;; ----------------------------------------------------------------------------

; 1. Törli az elemek hivatkozásait, levonja az elemek méretét a felmenő mappák
;    {:content-size ...} tulajdonságából, és frissíti a felmenő mappákat.
; 2. x mp elteltével, ha NEM történt meg az elemek visszaállítása, akkor véglegesen
;    törli az elemeket és azok leszármazott elemeit, illetve törli a fájlokat és bélyegképeket.

(defn delete-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id parent-id] :as mutation-props}]
  (when-let [{:media/keys [filename] :as file-item} (core.side-effects/get-item env item-id)]
            (core.side-effects/remove-item! env file-item)
            (core.side-effects/delete-file! filename)))

(defn delete-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id parent-id] :as mutation-props}]
  (when-let [directory-item (core.side-effects/get-item env item-id)]
            (core.side-effects/remove-item! env directory-item)
            (let [items (get directory-item :media/items)]
                 (doseq [{:media/keys [id]} items]
                        (if-let [{:media/keys [mime-type]} (core.side-effects/get-item env id)]
                                (let [mutation-props {:item-id id :parent-id item-id}]
                                     (case mime-type "storage/directory" (delete-directory-f env mutation-props)
                                                                         (delete-file-f      env mutation-props))))))))

(defn delete-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id parent-id] :as mutation-props}]
  (if-not (core.side-effects/item-attached? env parent-id {:media/id item-id})
          (if-let [{:media/keys [mime-type]} (core.side-effects/get-item env item-id)]
                  (case mime-type "storage/directory" (delete-directory-f env mutation-props)
                                                      (delete-file-f      env mutation-props)))))



;; -- Temporary delete item(s) mutations --------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-temporary-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id parent-id] :as mutation-props}]
  (when-let [media-item (core.side-effects/get-item env item-id)]
            (time/set-timeout! media-browser.config/PERMANENT-DELETE-AFTER #(delete-item-f env mutation-props))
            (core.side-effects/update-path-directories! env           media-item -)
            (core.side-effects/detach-item!             env parent-id media-item)
            (return item-id)))

(defn delete-items-temporary-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-ids parent-id]}]
  (letfn [(f [result item-id]
             (conj result (delete-item-temporary-f env {:item-id item-id :parent-id parent-id})))]
         (reduce f [] item-ids)))

(defmutation delete-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'storage.media-browser/delete-item!}
             (let [parent-id (item-browser/item-id->parent-id env :storage :media item-id)]
                  (delete-item-temporary-f env {:item-id item-id :parent-id parent-id})))

(defmutation delete-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'storage.media-browser/delete-items!}
             (let [parent-id (item-browser/item-id->parent-id env :storage :media (first item-ids))]
                  (delete-items-temporary-f env {:item-ids item-ids :parent-id parent-id})))



;; -- Undo delete item(s) mutations -------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item parent-id] :as mutation-props}]
  (when-let [media-item (core.side-effects/get-item env (:media/id item))]
            (core.side-effects/update-path-directories! env           media-item +)
            (core.side-effects/attach-item!             env parent-id media-item)
            (return media-item)))

(defn undo-delete-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [items parent-id]}]
  (letfn [(f [result item]
             (conj result (undo-delete-item-f env {:item item :parent-id parent-id})))]
         (reduce f [] items)))

(defmutation undo-delete-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item]}]
             {::pathom.co/op-name 'storage.media-browser/undo-delete-item!}
             (let [parent-id (item-browser/item->parent-id env :storage :media item)]
                  (undo-delete-item-f env {:item item :parent-id parent-id})))

(defmutation undo-delete-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [items]}]
             {::pathom.co/op-name 'storage.media-browser/undo-delete-items!}
             (let [parent-id (item-browser/item->parent-id env :storage :media (first items))]
                  (undo-delete-items-f env {:items items :parent-id parent-id})))



;; -- Duplicate item(s) mutations ---------------------------------------------
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
  (letfn [(f3 [{:media/keys [id filename] :as %}] (assoc % :media/filename (core.helpers/file-id->filename id filename)))
          (f2 [{:media/keys [id]          :as %}] (if (= id parent-id) {:media/id destination-id} %))
          (f1 [%]                                 (vector/->items % f2))]
         (as-> document % (mongo-db/duplicated-document-prototype request :media %)
                          (if (= destination-id parent-id) % (update % :media/path f1))
                          (f3 %))))

(defn duplicate-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props}]
  (let [prototype-f #(media-browser.prototypes/duplicated-file-prototype env mutation-props %)]
       (when-let [copy-item (mongo-db/duplicate-document! "storage" item-id {:prototype-f prototype-f})]
                 (if (= destination-id parent-id)
                     (core.side-effects/attach-item! env destination-id copy-item))
                 (core.side-effects/update-path-directories! env copy-item +)
                 (if-let [source-item (mongo-db/get-document-by-id "storage" item-id)]
                         (let [source-filename (get source-item :media/filename)
                               copy-filename   (get copy-item   :media/filename)]
                              (core.side-effects/duplicate-file! source-filename copy-filename)
                              (return copy-item))))))

(defn duplicate-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props}]
  (let [prototype-f #(media-browser.prototypes/duplicated-directory-prototype env mutation-props %)]
       (when-let [copy-item (mongo-db/duplicate-document! "storage" item-id {:prototype-f prototype-f})]
                 (when (= destination-id parent-id)
                       (core.side-effects/attach-item!             env destination-id copy-item)
                       (core.side-effects/update-path-directories! env                copy-item))
                 (let [items (get copy-item :media/items)]
                      (doseq [{:media/keys [id]} items]
                             (if-let [{:media/keys [mime-type]} (core.side-effects/get-item env id)]
                                     (let [destination-id (:id copy-item)
                                           mutation-props {:destination-id destination-id :item-id id :parent-id item-id}]
                                          (case mime-type "storage/directory" (duplicate-directory-f env mutation-props)
                                                                              (duplicate-file-f      env mutation-props))
                                          (return copy-item))))))))

(defn duplicate-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id parent-id]}]
  (if-let [{:media/keys [mime-type]} (core.side-effects/get-item env item-id)]
          (case mime-type "storage/directory" (duplicate-directory-f env {:item-id item-id :parent-id parent-id :destination-id parent-id})
                                              (duplicate-file-f      env {:item-id item-id :parent-id parent-id :destination-id parent-id}))))

(defn duplicate-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-ids parent-id]}]
  (letfn [(f [result item-id]
             (conj result (duplicate-item-f env {:item-id item-id :parent-id parent-id})))]
         (reduce f [] item-ids)))

(defmutation duplicate-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item]}]
             {::pathom.co/op-name 'storage.media-browser/duplicate-item!}
             (let [item-id   (get item :media/id)
                   parent-id (item-browser/item-id->parent-id env :storage :media item-id)]
                  (duplicate-item-f env {:item-id item-id :parent-id parent-id})))

(defmutation duplicate-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'storage.media-browser/duplicate-items!}
             (let [parent-id (item-browser/item-id->parent-id env :storage :media (first item-ids))]
                  (duplicate-items-f env {:item-ids item-ids :parent-id parent-id})))



;; -- Undo duplicate item(s) mutations ----------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-duplicate-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'storage.media-browser/undo-duplicate-item!}
             (let [parent-id (item-browser/item-id->parent-id env :storage :media item-id)]
                  (delete-item-temporary-f env {:item-id item-id :parent-id parent-id})))

(defmutation undo-duplicate-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'storage.media-browser/undo-duplicate-items!}
             (let [parent-id (item-browser/item-id->parent-id env :storage :media (first item-ids))]
                  (delete-items-temporary-f env {:item-ids item-ids :parent-id parent-id})))



;; -- Update item(s) mutations ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request]} {:keys [item]}]
  (mongo-db/save-document! "storage" item {:prototype-f #(mongo-db/updated-document-prototype request :media %)}))

(defmutation update-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env mutation-props]
             {::pathom.co/op-name 'storage.media-browser/update-item!}
             (update-item-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! delete-items! duplicate-item! duplicate-items! undo-delete-item!
               undo-delete-items! undo-duplicate-item! undo-duplicate-items! update-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
