
(ns elements.text-field.helpers
    (:require [candy.api                    :refer [return]]
              [elements.plain-field.helpers :as plain-field.helpers]
              [pretty-css.api               :as pretty-css]
              [re-frame.api                 :as r]
              [string.api                   :as string]
              [x.components.api             :as x.components]
              [x.environment.api            :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn static-adornment-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:color (keyword)
  ;  :icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-color (keyword)
  ;  :data-font-size (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)
  ;  :data-icon-family (keyword)}
  [field-id field-props {:keys [color icon icon-family]}]
  ; BUG#2105 (source-code/cljs/elements/plain_field/helpers.cljs)
  (merge (plain-field.helpers/field-accessory-attributes field-id field-props)
         {:data-line-height   :text-block
          :data-reveal-effect :delayed
          :data-selectable    false}
         (if icon {:data-icon-family icon-family})
         (if icon {:data-icon-size :s}
                  {:data-font-size :xs})))

(defn button-adornment-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:color (keyword)
  ;  :disabled? (boolean)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
  ;  :on-click (metamorphic-event)
  ;  :tab-indexed? (boolean)(opt)
  ;   Default: true
  ;  :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {}
  [field-id field-props {:keys [color disabled? icon icon-family on-click tab-indexed? tooltip]}]
  ; BUG#2105 (source-code/cljs/elements/plain_field/helpers.cljs)
  (merge (plain-field.helpers/field-accessory-attributes field-id field-props)
         {:data-color           color
          :data-click-effect    :opacity
          :data-line-height     :text-block
          :data-reveal-effect   :delayed
          :data-selectable      false
          :data-bubble-content  (x.components/content tooltip)
          :data-bubble-position :left}
         (if icon               {:data-icon-family icon-family
                                 :data-icon-size :s}
                                {:data-font-size :xs})
         (if disabled?          {:disabled   "1" :data-disabled true})
         (if-not tab-indexed?   {:tab-index "-1"})
         (if-not disabled?      {:on-mouse-up #(do (r/dispatch on-click)
                                                   (x.environment/blur-element!))})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-placeholder-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  ; HACK#9760 (source-code/cljs/elements/plain_field/helpers.cljs)
  {:data-font-size     :xs
   :data-line-height   :text-block
   :data-reveal-effect :delayed
   :data-selectable    false
   :data-text-overflow :hidden})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field-adornment-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {disabled? (boolean)
  ;  :icon (keyword)
  ;  :on-click (metamorphic-event)
  ;  :tooltip (metamorphic-content)}
  [field-id field-props]
  {:disabled? (plain-field.helpers/field-empty? field-id)
   :icon      :close
   :on-click  [:elements.text-field/empty-field! field-id field-props]
   :tooltip   :empty-field!})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-line-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:multiline? (boolean)(opt)}
  ;
  ; @return (integer)
  [field-id {:keys [multiline?]}]
  (let [field-content (plain-field.helpers/get-field-content field-id)]
       (if multiline? (let [line-count (-> field-content string/line-count inc)]
                           ; BUG#1481
                           ; Google Chrome Version 89.0.4389.114
                           ; The height of a textarea element has to be min. 2 rows!
                           ; Otherwise the browsers doesn't wraps the content in
                           ; every case.
                           (inc line-count))

                      ; If the field is NOT multiline ...
                      (return 1))))

(defn field-auto-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:font-size (keyword)
  ;  :line-height (keyword)}
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :native})
  ; =>
  ; "calc(var( --line-height-s ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :text-block})
  ; =>
  ; "calc(var( --text-block-height-s ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :xxl})
  ; =>
  ; "calc(var( --line-height-xxl ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height 48})
  ; =>
  ; "calc(48px * 1)"
  ;
  ; @return (string)
  [field-id {:keys [font-size line-height] :as field-props}]
  ; XXX#0886 (bithandshake/pretty-css)
  (let [line-count (field-line-count field-id field-props)]
       (case line-height :text-block (str "calc(var( --text-block-height-" (name font-size)   " ) * "line-count" + 12px)")
                         :native     (str "calc(var( --line-height-"       (name font-size)   " ) * "line-count" + 12px)")
                                     (str "calc(var( --line-height-"       (name line-height) " ) * "line-count" + 12px)"))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-emphasize-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:style (map)
  ;   {:height (string)}}
  [field-id field-props]
  {:style {:height (field-auto-height field-id field-props)}})

(defn input-container-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [border-color border-radius border-width style] :as field-props}]
  (-> {:style style}
      (pretty-css/border-attributes field-props)
      (pretty-css/font-attributes   field-props)
      (pretty-css/indent-attributes field-props)
      (pretty-css/marker-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-input-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  (merge (plain-field.helpers/field-input-attributes field-id field-props)
         {:data-reveal-effect :delayed
          :max-length         max-length
          :type               type}
         (if disabled? {}
                       {:auto-complete autofill-name
                        :min           date-from
                        :max           date-to
                        :name          autofill-name})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:min-width (keyword)
  ;  :stretch-orientation (keyword)}
  ;
  ; @return (map)
  ; {:data-element-min-width (keyword)
  ;  :data-stretch-orientation (keyword)}
  [_ {:keys [min-width stretch-orientation] :as field-props}]
  (-> {:data-element-min-width   min-width
       :data-stretch-orientation stretch-orientation}
      (pretty-css/default-attributes field-props)
      (pretty-css/outdent-attributes field-props)))
