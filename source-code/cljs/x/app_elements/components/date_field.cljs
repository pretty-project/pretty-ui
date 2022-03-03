
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.date-field
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:indent (keyword)
  ;   :name (keyword)
  ;   :type (keyword)
  ;   :value-path (item-path vector)}
  [field-id field-props]
  (merge {:indent     :none
          :type       :date
          :value-path (engine/default-value-path field-id)}
         (param field-props)
          ; XXX#6782
         {:name (a/id)}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-date-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (merge (r engine/get-element-props db field-id)
         (r engine/get-input-props   db field-id)))

(a/reg-sub :elements/get-date-field-props get-date-field-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- date-field-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.x-date-field--label [components/content label]]))

(defn- date-field-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:input.x-date-field--input (engine/field-body-attributes field-id field-props)])

(defn- date-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:label.x-date-field (engine/element-attributes field-id field-props)
                       [date-field-label          field-id field-props]
                       [date-field-body           field-id field-props]])

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :date-from (string)(opt)
  ;    TODO ...
  ;   :date-to (string)(opt)
  ;    TODO ...
  ;   :disabled? (boolean)(opt)
  ;    TODO ...
  ;   :form-id (keyword)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  [elements/date-field {...}]
  ;
  ; @usage
  ;  [elements/date-field :my-date-field {...}]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (field-props-prototype field-id field-props)]
        [engine/stated-element field-id
                               {:render-f      #'date-field
                                :element-props field-props
                                :initializer   [:elements/init-field!          field-id]
                                :subscriber    [:elements/get-date-field-props field-id]}])))
