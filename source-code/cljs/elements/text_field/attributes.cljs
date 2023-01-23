
(ns elements.text-field.attributes
    (:require [dom.api                         :as dom]
              [elements.plain-field.attributes :as plain-field.attributes]
              [elements.text-field.helpers     :as text-field.helpers]
              [pretty-css.api                  :as pretty-css]
              [re-frame.api                    :as r]
              [x.components.api                :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (metamorphic-event)
  ;  :tab-indexed? (boolean)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {}
  [field-id field-props {:keys [disabled? on-click tab-indexed? tooltip-content] :as adornment-props}]
  ; BUG#2105 (source-code/cljs/elements/plain_field/helpers.cljs)
  (-> (plain-field.attributes/field-accessory-attributes field-id field-props)
      (merge {:class                 :e-text-field--adornment
              :data-selectable       false
              :data-tooltip-content  (x.components/content tooltip-content)
              :data-tooltip-position :left}
             (if disabled?        {:disabled   "1" :data-disabled true})
             (if-not tab-indexed? {:tab-index "-1"})
             (if-not disabled?    {:on-mouse-up #(do (r/dispatch on-click)
                                                     (dom/blur-active-element!))}))
      (pretty-css/color-attributes  adornment-props)
      (pretty-css/effect-attributes adornment-props)
      (pretty-css/font-attributes   adornment-props)
      (pretty-css/icon-attributes   adornment-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [_ field-props]
  ; HACK#9760 (source-code/cljs/elements/plain_field/helpers.cljs)
  (-> {:class               :e-text-field--placeholder
       :data-font-size      :xs
       :data-letter-spacing :auto
       :data-line-height    :text-block
       :data-selectable     false
       :data-text-overflow  :hidden}
      (pretty-css/effect-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-emphasize-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)
  ;   {:height (string)}}
  [field-id field-props]
  {:class :e-text-field--input-emphasize
   :style {:height (text-field.helpers/field-auto-height field-id field-props)}})

(defn input-container-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [style] :as field-props}]
  (-> {:class               :e-text-field--input-container
       :data-letter-spacing :auto
       :style               style}
      (pretty-css/border-attributes field-props)
      (pretty-css/font-attributes   field-props)
      (pretty-css/indent-attributes field-props)
      (pretty-css/marker-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-adornments-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [field-id field-props]
  (merge (plain-field.attributes/field-accessory-attributes field-id field-props)
         {:class :e-text-field--adornments-placeholder}))

(defn field-surface-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [field-id field-props]
  (merge (plain-field.attributes/field-surface-attributes field-id field-props)
         {:class :e-tex-field--field-surface}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-input-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autofill-name (keyword)
  ;  :date-from (string)(opt)
  ;  :date-to (string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :max-length (integer)(opt)
  ;  :type (keyword)(opt)
  ;   :password, :text}
  ;
  ; @return (map)
  ; {:auto-complete (keyword)
  ;  :max (string)
  ;  :max-length (integer)
  ;  :min (string)
  ;  :name (keyword)
  ;  :type (keyword)}
  [field-id {:keys [autofill-name date-from date-to disabled? max-length type] :as field-props}]
  ; HACK#9760 (source-code/cljs/elements/plain_field/helpers.cljs)
  ;
  ; The {:type :date} fields range could being set by the :min and :max properties.
  (-> (plain-field.attributes/field-input-attributes field-id field-props)
      (merge {:max-length max-length
              :type       type}
             (if-not disabled? {:auto-complete autofill-name
                                :min           date-from
                                :max           date-to
                                :name          autofill-name}))
      (pretty-css/effect-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:stretch-orientation (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-stretch-orientation (keyword)}
  [_ {:keys [stretch-orientation] :as field-props}]
  (-> {:class                    :e-text-field
       :data-stretch-orientation stretch-orientation}
      (pretty-css/default-attributes          field-props)
      (pretty-css/element-min-size-attributes field-props)
      (pretty-css/outdent-attributes          field-props)))
