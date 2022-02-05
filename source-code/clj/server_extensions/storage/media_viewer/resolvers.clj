
(ns server-extensions.storage.media-viewer.resolvers
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.api     :as mongo-db]
              [pathom.api       :as pathom]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-directory-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env _]
  (let [directory-id (pathom/env->param env :directory-id)]
       (merge (mongo-db/get-document-by-id "storage" directory-id)
              ; First image or pdf ...
              {:media/default-item ""})))

(defresolver get-directory-item
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:storage.media-viewer/get-directory-item (namespaced map)
             [env resolver-props]
             {:storage.media-viewer/get-directory-item (get-directory-item-f env resolver-props)})



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-directory-item])

(pathom/reg-handlers! ::handlers HANDLERS)
