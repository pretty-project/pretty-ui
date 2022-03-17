
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.directory-creator.mutations
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [extensions.storage.side-effects       :as side-effects]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:keys [alias destination-id]}]
  (if-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
          (let [destination-path (get  destination-item :media/path)
                directory-path   (conj destination-path {:media/id destination-id})
                directory-item {:media/alias alias :media/content-size 0 :media/description ""
                                :media/items []    :media/path directory-path
                                :media/mime-type "storage/directory"}]
               (when-let [directory-item (side-effects/insert-item! env directory-item)]
                         (side-effects/attach-item!             env destination-id directory-item)
                         (side-effects/update-path-directories! env directory-item +)))))

(defmutation create-directory!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env mutation-props]
             {::pathom.co/op-name 'storage.directory-creator/create-directory!}
             (create-directory-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [create-directory!])

(pathom/reg-handlers! ::handlers HANDLERS)
