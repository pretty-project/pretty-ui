
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.password-field
    (:require [mid-fruits.candy                             :refer [param]]
              [mid-fruits.form                              :as form]
              [x.app-core.api                               :as a :refer [r]]
              [x.app-elements.element-components.text-field :as element-components.text-field :refer [text-field]]
              [x.app-elements.engine.api                    :as engine]
              [x.app-elements.passfield-handler.subs        :as passfield-handler.subs]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:validate? (boolean)}
  ;
  ; @return (map)
  ;  {:info-tooltip (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :type (keyword)(opt)
  ;   :validator (map)
  ;    {:f (function)
  ;     :invalid-message (keyword)}}
  [field-id {:keys [validate?] :as field-props}]
  (merge {:label :password
          :type  :password
          :value-path (engine/default-value-path field-id)}
         (param field-props)
         (if validate? {:tooltip   :valid-password-rules
                        :validator {:f form/password?
                                    :invalid-message :password-is-too-weak}})))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-password-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @param (map)
  [db [_ field-id]]
  (merge (r element-components.text-field/get-text-field-props db field-id)
         (r passfield-handler.subs/get-passfield-props         db field-id)))

(a/reg-sub :elements/get-password-field-props get-password-field-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;   :border-color (keyword)(opt)
  ;    :default, :primary, :secondary
  ;    Default: :default
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :form-id (keyword)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :initial-value (string)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Only w/o {:placeholder ...}
  ;   :max-length (integer)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :on-blur (metamorphic-event)(constant)(opt)
  ;   :on-change (metamorphic-event)(constant)(opt)
  ;   :on-focus (metamorphic-event)(constant)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :validate? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/password-field {...}]
  ;
  ; @usage
  ;  [elements/password-field :my-password-field {...}]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (as-> field-props % (field-props-prototype                               field-id %)
                                         (element-components.text-field/field-props-prototype field-id %))]
        [engine/stated-element field-id
                               {:render-f      #'text-field
                                :element-props field-props
                                :initializer   [:elements/init-field!              field-id]
                                :subscriber    [:elements/get-password-field-props field-id]}])))
