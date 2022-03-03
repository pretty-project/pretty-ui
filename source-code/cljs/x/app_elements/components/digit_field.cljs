
; WARNING! NOT TESTED! DO NOT USE!

;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.digit-field
    (:require [app-fruits.dom            :as dom]
              [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def DIGIT-WIDTH 36)

; @constant (px)
(def DIGIT-GAP 12)



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
     (* DIGIT-GAP   3)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:digit-count (integer)
  ;   :value-path (item-path vector)}
  [field-id field-props]
  (merge {:digit-count 4
          :value-path  (engine/default-value-path field-id)}
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

(defn- digit-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  [:input.x-digit-field--input {:type "text"
                                :id (engine/element-id->target-id field-id)
                                :on-change #(let [v (dom/event->value %)]
                                                 (a/dispatch-sync [:db/set-item! (:value-path field-props) (str v)]))}])

(defn- digit-field-cover
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  (reduce (fn [%1 %2] (conj %1 [:div.x-digit-field--cover--digit {:on-mouse-up #(dom/focus-element! (dom/get-element-by-id (engine/element-id->target-id field-id)))
                                                                  ; prevent selecting
                                                                  :on-mouse-down #(.preventDefault %)}
                                                                 (mid-fruits.string/get-nth-character (:value field-props) %2)]))
    [:div.x-digit-field--cover {:style {:width (-> field-props field-props->digits-width css/px)}}]
    (range 4)))

(defn- digit-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  [:div.x-digit-field (engine/element-attributes field-id field-props)
                      [digit-field-input         field-id field-props]
                      [digit-field-cover         field-id field-props]])

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:digit-count (integer)
  ;    Default: 4
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :value-path (item-path vector)}
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
   (let [field-props (field-props-prototype field-id field-props)]
        [engine/stated-element field-id
                               {:render-f      #'digit-field
                                :element-props field-props
                                :subscriber    [:elements/get-digit-field-props field-id]}])))
