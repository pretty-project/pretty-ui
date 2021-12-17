
(ns server-plugins.item-editor.sample
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.api     :as mongo-db]
              [pathom.api       :as pathom]
              [server-plugins.item-editor.api        :as item-editor]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-my-type-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:my-type/id (string)}
             ;
             ; @return (map)
             ;  {:my-extension/get-my-type-item (map)}
             [env {:keys [my-type/id]}]
             {::pco/output [:my-type/id
                            :my-type/added-at
                            :client/archived?
                            :client/favorite?
                            :my-type/modified-at]}
             (if-let [document (mongo-db/get-document-by-id :my-collection id)]
                     (return document)
                     (pathom/error-answer :document-not-found)))

    ; @name multi-view?
    ;  TODO ...



    ;; -- Usage -------------------------------------------------------------------
    ;; ----------------------------------------------------------------------------

    ; @usage
    ;  More info: app-plugins.item-editor.api
    ;
    ; @usage
    ;  (ns my-namespace (:require [server-plugins.item-editor.api :as item-editor]))
    ;
    ; @usage
    ;  (defresolver get-my-type-item [_ {:keys [my-type/id]}] ...)
    ;  (defmutation update-my-type-item!    [_] ...)
    ;  (defmutation add-my-type-item!       [_] ...)
    ;  (defmutation delete-my-type-item!    [_] ...)
    ;  (defmutation duplicate-my-type-item! [_] ...)



; (pathom/error-answer ) válaszra reagál az item-editor kliens-oldala
