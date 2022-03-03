
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.multi-field
    (:require [mid-fruits.candy                     :as candy :refer [param return]]
              [mid-fruits.loop                      :refer [reduce-indexed]]
              [mid-fruits.vector                    :as vector]
              [x.app-components.api                 :as components]
              [x.app-core.api                       :as a :refer [r]]
              [x.app-elements.components.text-field :rename {element text-field}]
              [x.app-elements.engine.api            :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-props->single-field?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:group-value (vector)}
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [_ {:keys [group-value]} _]
  (vector/count? group-value 1))

(defn- group-props->multi-field?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props field-dex]
  (not (group-props->single-field? group-id group-props field-dex)))

(defn- group-props->max-field-count-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:group-value (vector)
  ;   :max-input-count (integer)}
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [_ {:keys [group-value max-input-count]} _]
  (vector/count? group-value max-input-count))

(defn- field-dex->last-field?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:group-value (vector)}
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [_ {:keys [group-value]} field-dex]
  (vector/dex-last? group-value field-dex))

(defn- field-dex->field-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:label (metamorphic-content)}
  ; @param (integer) field-dex
  ;
  ; @return (metamorphic-content)
  [group-id {:keys [label] :as group-props} field-dex]
        ; Single-field label
  (cond (and    label (group-props->single-field? group-id group-props field-dex))
        (return label)
        ; Multi-field label
        (and label (group-props->multi-field? group-id group-props field-dex))
        (components/content {:content label :suffix (str " #" (inc field-dex))})))

(defn- field-dex->end-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (maps in vector)
  [group-id group-props field-dex]
        ; Multiple field & last field & maximum field count reached
  (cond (and (field-dex->last-field?                group-id group-props field-dex)
             (group-props->multi-field?             group-id group-props field-dex)
             (group-props->max-field-count-reached? group-id group-props field-dex))
        [{:icon :close :on-click [:elements/decrease-input-count! group-id field-dex]
          :tooltip :delete-field!}]
        ; Multiple field & last field
        (and (field-dex->last-field?    group-id group-props field-dex)
             (group-props->multi-field? group-id group-props field-dex))
        [{:icon :add :on-click [:elements/increase-input-count! group-id {:initial-value ""}]
          :tooltip :add-field!}
         {:icon :close :on-click [:elements/decrease-input-count! group-id field-dex]
          :tooltip :delete-field!}]
        ; Single field
        (group-props->single-field? group-id group-props field-dex)
        [{:icon :add :on-click [:elements/increase-input-count! group-id {:initial-value ""}]
          :tooltip :add-field!}]
        ; Single field & not the last field
        (group-props->multi-field? group-id group-props field-dex)
        [{:icon :close :on-click [:elements/decrease-input-count! group-id field-dex]
          :tooltip :delete-field!}]))

(defn- field-dex->auto-focus?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:value-path (item-path vector)}
  ; @param (integer) field-dex
  ;
  ; @return (item-path vector)
  [group-id {:keys [input-count-increased?] :as group-props} field-dex]
  (and (boolean input-count-increased?)
       (field-dex->last-field? group-id group-props field-dex)))

(defn- field-dex->value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:value-path (item-path vector)}
  ; @param (integer) field-dex
  ;
  ; @return (item-path vector)
  [_ {:keys [value-path]} field-dex]
  (vector/conj-item value-path field-dex))

(defn- field-dex->react-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @example
  ;  (field-dex->react-key :my-group {} 3)
  ;  =>
  ;  "my-group--3"
  ;
  ; @return (keyword)
  [group-id _ field-dex]
  (a/dom-value group-id field-dex))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {:disallow-empty-input-group? (boolean)
  ;   :max-input-count (integer)
  ;   :value-path (item-path vector)}
  [group-id group-props]
  (merge {:max-input-count 8
          :value-path      (engine/default-value-path group-id)}
         (param group-props)
         {:disallow-empty-input-group? true}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-multi-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @param (map)
  [db [_ group-id]]
  (merge (r engine/get-element-props     db group-id)
         (r engine/get-input-group-props db group-id)))

(a/reg-sub :elements/get-multi-field-props get-multi-field-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-field-text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:placeholder (metamorphic-content)(opt)}
  ; @param (integer) field-dex
  [group-id {:keys [placeholder] :as group-props} field-dex]
  [:div.x-multi-field--text-field
    {:key (field-dex->react-key group-id group-props field-dex)}
    [text-field {:auto-focus?    (field-dex->auto-focus?    group-id group-props field-dex)
                 :end-adornments (field-dex->end-adornments group-id group-props field-dex)
                 :label          (field-dex->field-label    group-id group-props field-dex)
                 :layout         (param :fit)
                 :placeholder    (param placeholder)
                 :value-path     (field-dex->value-path     group-id group-props field-dex)}]])

(defn- multi-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:group-value (vector)}
  [group-id {:keys [group-value] :as group-props}]
  (reduce-indexed (fn [wrapper field-dex _]
                      (conj wrapper [multi-field-text-field group-id group-props field-dex]))
                  [:div.x-multi-field (engine/element-attributes group-id group-props)]
                  (param group-value)))

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;    TODO ...
  ;   :info-tooltip (metamorphic-content)(opt)
  ;    TODO ...
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :label (metamorphic-content)(opt)
  ;   :max-field-count (integer)(opt)
  ;    Default: 8
  ;   :placeholder (metamorphic-content)(opt)
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/multi-field {...}]
  ;
  ; @usage
  ;  [elements/multi-field :my-multi-field {...}]
  ([group-props]
   [element (a/id) group-props])

  ([group-id group-props]
   (let [group-props (group-props-prototype group-id group-props)]
        [engine/stated-element group-id
                               {:render-f      #'multi-field
                                :element-props group-props
                                :subscriber    [:elements/get-multi-field-props group-id]}])))
