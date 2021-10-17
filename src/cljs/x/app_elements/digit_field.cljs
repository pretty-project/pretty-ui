
; WARNING! NOT TESTED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.05
; Description:
; Version: v0.1.6
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.digit-field
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def DIGIT-WIDTH 36)

; @constant (px)
(def DIGIT-SPACE 12)

; @constant (integer)
(def DEFAULT-DIGITS-COUNT 4)



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-props->digits-width
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (integer)
  [view-props]
  (+ (* DIGIT-WIDTH 4)
     (* DIGIT-SPACE 3)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:digits-count (integer)}
  [field-props]
  (merge {:digits-count DEFAULT-DIGITS-COUNT}
         (param field-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (merge (r engine/get-element-view-props   db field-id)
         (r engine/get-field-view-props     db field-id)
         (r engine/get-passfield-view-props db field-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- digit
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:div.x-digit-field--digit])

(defn- digits
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  (reduce #(vector/conj-item %1 [digit])
           [:div.x-digit-field--digits
             {:style {:width (css/px (view-props->digits-width view-props))}}]
           (range 4)))

(defn- digit-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:div.x-digit-field
    (engine/element-attributes field-id view-props)
    [digits field-id view-props]])

(defn view
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:digits-count (integer)
  ;    Default: DEFAULT-DIGITS-COUNT}
  ;
  ; @usage
  ;  [elements/digit-field {...}]
  ;
  ; @usage
  ;  [elements/digit-field :my-digit-field {...}]
  ;
  ; @return (component)
  ([field-props]
   [view nil field-props])

  ([field-id field-props]
   (let [field-id    (a/id   field-id)
         field-props (a/prot field-props field-props-prototype)]
        [engine/container field-id
          {:base-props field-props
           :component  digit-field
           :subscriber [::get-view-props field-id]}])))
