
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.3.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.submit-button
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-core.api        :as a :refer [r]]
              [x.app-elements.button :as button :refer [button]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- submit-button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {:label (metamorphic-content)}
  [button-props]
  (merge {:label :submit!}
         (param button-props)))

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;
  ; @return (map)
  [button-props]
  (let [submit-button-props (submit-button-props-prototype button-props)]
       (button/button-props-prototype submit-button-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-id
  ;
  ; @return (map)
  ;  {:disabled? (boolean)}
  [db [_ button-id]]
  (merge (r engine/get-element-view-props db button-id)
         (if-not (r engine/inputs-passed? db button-id)
                 {:disabled? true})))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
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
  ;
  ; @return (component)
  ([button-props]
   [view (a/id) button-props])

  ([button-id button-props]
   (let [button-props (a/prot button-props button-props-prototype)]
        [engine/stated-element button-id
                               {:component     #'button
                                :element-props button-props
                                :destructor    [:elements/destruct-clickable! button-id]
                                :initializer   [:elements/init-clickable!     button-id]
                                :subscriber    [::get-view-props              button-id]}])))
