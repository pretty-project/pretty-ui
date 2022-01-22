
(ns server-extensions.storage.media-browser.mutations
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.api     :as mongo-db]
              [pathom.api       :as pathom]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [server-extensions.storage.engine      :as engine]
              [server-plugins.item-browser.api       :as item-browser]))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  [env {:keys [alias destination-id]}]
  (if-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
          (let [destination-path (get  destination-item :media/path)
                directory-path   (conj destination-path {:media/id destination-id})
                directory-item {:media/alias alias :media/content-size 0 :media/description ""
                                :media/items []    :media/path directory-path
                                :media/mime-type "storage/directory"}]
               (when-let [{:media/keys [id]} (engine/insert-media-item! env directory-item)]
                         (engine/attach-media-item!       env destination-id id)
                         (engine/update-path-directories! env directory-item +)))))

(defmutation create-directory!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'storage/create-directory!}
             (create-directory-f env mutation-props))

(defn delete-file-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (string)
  [env {:keys [item-id] :as mutation-props}]
  (if-let [file-item (engine/get-media-item env item-id)]
          (when-let [parent-id (item-browser/item->parent-id :storage :media file-item)]
                    (engine/detach-media-item!       env parent-id item-id)
                    (engine/remove-media-item!       env   item-id)
                    (engine/update-path-directories! env file-item -)
                    (return item-id))))

(defn delete-directory-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (string)
  [env {:keys [item-id] :as mutation-props}]
  (if-let [directory-item (engine/get-media-item env item-id)]
          (when-let [parent-id (item-browser/item->parent-id :storage :media directory-item)]
                    (engine/detach-media-item!       env parent-id item-id)
                    (engine/remove-media-item!       env   item-id)
                    (engine/update-path-directories! env directory-item -)
                    (let [items (get directory-item :media/items)]
                         (doseq [{:media/keys [id]} items]
                                (when-let [{:media/keys [mime-type]} (engine/get-media-item env id)]
                                          (case mime-type "storage/directory" (delete-directory-item-f env {:item-id id})
                                                                              (delete-file-item-f      env {:item-id id}))
                                          (return item-id)))))))

(defn delete-media-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (?)
  [env {:keys [item-id] :as mutation-props}]
  (if-let [{:media/keys [mime-type]} (engine/get-media-item env item-id)]
          (case mime-type "storage/directory" (delete-directory-item-f env mutation-props)
                                              (delete-file-item-f      env mutation-props))))

(defn delete-media-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-ids (string in vector)}
  ;
  ; @return (strings in vector)
  [env {:keys [item-ids]}]
  (reduce #(conj %1 (delete-media-item-f env {:item-id %2})) [] item-ids))

(defmutation delete-media-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (?)
             [env mutation-props]
             {::pathom.co/op-name 'storage/delete-media-items!}
             (delete-media-items-f env mutation-props))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [create-directory! delete-media-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
