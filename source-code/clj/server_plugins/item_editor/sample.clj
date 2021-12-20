
(ns server-plugins.item-editor.sample
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.string    :as string]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-db.api      :as db]
              [server-plugins.item-editor.api        :as item-editor]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; -  Az [:item-editor/initialize! {...}] esemény számára {:suggestion-keys [...]} tulajdonságként
;    átadott kulcsokhoz tartozó értékeket a get-my-type-suggestions resolver használatával
;    tölti le a kliens-oldali item-editor plugin.
; - A {:suggestion-keys [...]} tulajdonság használatához szükséges létrehozni a get-my-type-suggestions
;   formulat alapján elnevezett resolver függvényt!
(defresolver get-my-type-suggestions
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ;  {}
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:my-extension/get-my-type-suggestions (map)
             [env _]
             {:my-extension/get-my-type-suggestions
              (let [all-documents     (mongo-db/get-all-documents  :my-collection)
                    suggestion-keys   (pathom/env->param       env :suggestion-keys)
                    suggestion-values (db/get-specified-values all-documents suggestion-keys string/nonempty?)]
                   (validator/validate-data suggestion-values))})

(defresolver get-my-type-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:my-type/id (string)}
             ;
             ; @return (map)
             ;  {:my-extension/get-my-type-item (map)}
             [env {:keys [my-type/id]}]
             ; A felsorolt tulajdonságok szükségesek az item-editor plugin működéséhez:
             {::pco/output [:my-type/id
                            :my-type/added-at
                            :my-type/archived?
                            :my-type/description
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
