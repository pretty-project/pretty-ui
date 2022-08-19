
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.submit-button
    (:require [mid-fruits.candy                         :refer [param]]
              [x.app-core.api                           :as a :refer [r]]
              [x.app-elements.element-components.button :as element-components.button :refer [button]]
              [x.app-elements.engine.api                :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {:label (metamorphic-content)}
  [button-props]
  (merge {:label :submit!}
         (param button-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-submit-button-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-id
  ;
  ; @return (map)
  ;  {:disabled? (boolean)}
  [db [_ button-id]]
  (merge (r engine/get-element-props db button-id)
         (if-not (r engine/inputs-passed? db button-id)
                 {:disabled? true})))

(a/reg-sub :elements/get-submit-button-props get-submit-button-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0714
  ; A submit-button elem alapkomponense a button elem.
  ; A submit-button elem további paraméterezését a button elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  {:input-ids (keywords in vector)(constant)(opt)
  ;    A továbblépéshez validálni és kitölteni szükséges mezők azonosítói
  ;   :label (metamorphic content)(opt)
  ;    Default: :submit!}
  ;
  ; @usage
  ;  [elements/submit-button {...}]
  ;
  ; @usage
  ;  [elements/submit-button :my-submit-button {...}]
  ([button-props]
   [element (a/id) button-props])

  ([button-id button-props]
   (let [button-props (-> button-props button-props-prototype element-components.button/button-props-prototype)]
        [engine/stated-element button-id
                               {:render-f      #'button
                                :element-props button-props
                                :destructor    [:elements/destruct-clickable!     button-id]
                                :initializer   [:elements/init-clickable!         button-id]
                                :subscriber    [:elements/get-submit-button-props button-id]}])))
