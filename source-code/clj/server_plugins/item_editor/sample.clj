
(ns server-plugins.item-editor.sample
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
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
             ; A {:my-type/archived? ...}, {:my-type/favorite? ...} és {:my-type/modified-at ...}
             ; tulajdonságok az item-editor plugin működéséhez szükségesek.
             {::pco/output [:my-type/id
                            :my-type/added-at
                            :my-type/archived?
                            :my-type/favorite?
                            :my-type/modified-at]}
             (if-let [document (mongo-db/get-document-by-id :my-collection id)]
                     ; A validator alkalmazása nélkül a kliens-oldalra küldött dokumentumot
                     ; az item-editor plugin nem nyitja meg, helyette egy hibaüzenetet jelenít meg.
                     (validator/validate-data document)))







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
