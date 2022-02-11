
(ns server-extensions.storage.media-browser.mutations
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.time   :as time]
              [mid-fruits.vector :as vector]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [server-fruits.io  :as io]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [server-extensions.storage.engine      :as engine]
              [server-plugins.item-browser.api       :as item-browser]))



;; -- Delete item(s) mutations ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id parent-id] :as mutation-props}]
  (letfn [; A delete-f függvény a file-item eltávolítása után 60 másodperccel kitörli a fájlt,
          ; ha az eltelt idő alatt a file-item nem lett visszaállítva ...
          (delete-f [{:keys [filename id]}] (if-not (mongo-db/document-exists? "storage" id)
                                                    (engine/delete-file! filename)))]
         (when-let [file-item (engine/get-item env item-id)]
                   (engine/detach-item!             env parent-id file-item)
                   (engine/remove-item!             env           file-item)
                   (engine/update-path-directories! env           file-item -)
                   (time/set-timeout! 60000 #(delete-f file-item))
                   (return item-id))))

(defn delete-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id parent-id] :as mutation-props}]
  (when-let [directory-item (engine/get-item env item-id)]
            (engine/detach-item!             env parent-id directory-item)
            (engine/remove-item!             env           directory-item)
            (engine/update-path-directories! env           directory-item)
            (let [items (get directory-item :media/items)]
                 (doseq [{:media/keys [id]} items]
                        (if-let [{:media/keys [mime-type]} (engine/get-item env id)]
                                (let [mutation-props {:item-id id :parent-id item-id}]
                                     (case mime-type "storage/directory" (delete-directory-f env mutation-props)
                                                                         (delete-file-f      env mutation-props))
                                     (return item-id)))))))

(defn delete-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id] :as mutation-props}]
  (if-let [{:media/keys [mime-type]} (engine/get-item env item-id)]
          (case mime-type "storage/directory" (delete-directory-f env mutation-props)
                                              (delete-file-f      env mutation-props))))

(defn delete-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-ids]}]
  (let [parent-id (item-browser/item-id->parent-id :storage :media (first item-ids))]
       (letfn [(f [result item-id]
                  (conj result (delete-item-f env {:item-id item-id :parent-id parent-id})))]
              (reduce f [] item-ids))))

(defmutation delete-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env mutation-props]
             {::pathom.co/op-name 'storage.media-lister/delete-items!}
             (delete-items-f env mutation-props))

(defmutation delete-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'storage.media-browser/delete-item!}
             (delete-items-f env {:item-ids [item-id]}))



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
  (letfn [(f3 [{:media/keys [id filename] :as %}] (assoc % :media/filename (engine/file-id->filename id filename)))
          (f2 [{:media/keys [id]          :as %}] (if (= id parent-id) {:media/id destination-id} %))
          (f1 [%]                                 (vector/->items % f2))]
         (as-> document % (mongo-db/duplicated-document-prototype request :media %)
                          (if (= destination-id parent-id) % (update % :media/path f1))
                          (f3 %))))

(defn duplicate-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props}]
  (when-let [copy-item (mongo-db/duplicate-document! "storage" item-id
                                                     {:prototype-f #(duplicated-file-prototype env mutation-props %)})]
            (if (= destination-id parent-id)
                (engine/attach-item! env destination-id copy-item))
            (engine/update-path-directories! env copy-item +)
            (if-let [source-item (mongo-db/get-document-by-id "storage" item-id)]
                    (let [source-filename (get source-item :media/filename)
                          copy-filename   (get copy-item   :media/filename)]
                         (engine/duplicate-file! source-filename copy-filename)
                         (return copy-item)))))

(defn duplicate-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props}]
  (when-let [copy-item (mongo-db/duplicate-document! "storage" item-id
                                                     {:prototype-f #(duplicated-directory-prototype env mutation-props %)})]
            (when (= destination-id parent-id)
                  (engine/attach-item!             env destination-id copy-item)
                  (engine/update-path-directories! env                copy-item))
            (let [items (get copy-item :media/items)]
                 (doseq [{:media/keys [id]} items]
                        (if-let [{:media/keys [mime-type]} (engine/get-item env id)]
                                (let [destination-id (:id copy-item)
                                      mutation-props {:destination-id destination-id :item-id id :parent-id item-id}]
                                     (case mime-type "storage/directory" (duplicate-directory-f env mutation-props)
                                                                         (duplicate-file-f      env mutation-props))
                                     (return copy-item)))))))

(defn duplicate-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-id] :as mutation-props}]
  (if-let [{:media/keys [mime-type]} (engine/get-item env item-id)]
          (case mime-type "storage/directory" (duplicate-directory-f env mutation-props)
                                              (duplicate-file-f      env mutation-props))))

(defn duplicate-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [item-ids]}]
  (let [parent-id (item-browser/item-id->parent-id :storage :media (first item-ids))]
       (letfn [(f [result item-id]
                  (conj result (duplicate-item-f env {:item-id item-id :destination-id parent-id :parent-id parent-id})))]
              (reduce f [] item-ids))))

(defmutation duplicate-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env mutation-props]
             {::pathom.co/op-name 'storage.media-lister/duplicate-items!}
             (duplicate-items-f env mutation-props))

(defmutation duplicate-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'storage.media-browser/duplicate-item!}
             (duplicate-items-f env {:item-ids [item-id]}))



;; -- Update item(s) mutations ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request]} media-item]
  (mongo-db/save-document! "storage" media-item
                           {:prototype-f #(mongo-db/updated-document-prototype request :media %)}))

(defmutation update-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env media-item]
             {::pathom.co/op-name 'storage.media-browser/update-item!}
             (update-item-f env media-item))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! delete-items! duplicate-item! duplicate-items! update-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
