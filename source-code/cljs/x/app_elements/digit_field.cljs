
; WARNING! NOT TESTED! DO NOT USE!

;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.05
; Description:
; Version: v0.1.8
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.digit-field
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def DIGIT-WIDTH 36)

; @constant (px)
(def DIGIT-SPACE 12)

; @constant (integer)
(def DEFAULT-DIGIT-COUNT 4)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props->digits-width
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (integer)
  [field-props]
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
  ;  {:digit-count (integer)}
  [field-props]
  (merge {:digit-count DEFAULT-DIGIT-COUNT}
         (param field-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-digit-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (merge (r engine/get-element-props   db field-id)
         (r engine/get-field-props     db field-id)
         (r engine/get-passfield-props db field-id)))

(a/reg-sub :elements/get-digit-field-props get-digit-field-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- digit
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  [:div.x-digit-field--digit])

(defn- digits
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  (vec (reduce #(conj %1 [digit])
                [:div.x-digit-field--digits {:style {:width (css/px (field-props->digits-width field-props))}}]
                (range 4))))

(defn- digit-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  [:div.x-digit-field (engine/element-attributes field-id field-props)
                      [digits                    field-id field-props]])

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:digit-count (integer)
  ;    Default: DEFAULT-DIGIT-COUNT
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none}
  ;
  ; @usage
  ;  [elements/digit-field {...}]
  ;
  ; @usage
  ;  [elements/digit-field :my-digit-field {...}]
  ;
  ; @return (component)
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (field-props-prototype field-props)]
        [engine/stated-element field-id
                               {:component     #'digit-field
                                :element-props field-props
                                :subscriber    [:elements/get-digit-field-props field-id]}])))
