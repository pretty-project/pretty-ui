
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.media-viewer.resolvers
    (:require [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mid-fruits.candy                      :refer [param return]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-directory-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env _]
  (let [directory-id (pathom/env->param env :directory-id)]
       (merge (mongo-db/get-document-by-id "storage" directory-id)
              ; First image or pdf ...
              {:media/default-item ""})))

(defresolver get-directory-item
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env resolver-props]
             {:storage.media-viewer/get-directory-item (get-directory-item-f env resolver-props)})



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-directory-item])

(pathom/reg-handlers! ::handlers HANDLERS)
