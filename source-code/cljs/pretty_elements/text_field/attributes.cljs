
(ns pretty-elements.text-field.attributes
    (:require [dom.api                                :as dom]
              [fruits.random.api                      :as random]
              [metamorphic-content.api                :as metamorphic-content]
              [pretty-build-kit.api                         :as pretty-build-kit]
              [pretty-elements.input.env              :as input.env]
              [pretty-elements.plain-field.attributes :as plain-field.attributes]
              [pretty-elements.text-field.env         :as text-field.env]
              [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)
  ;  :tab-indexed? (boolean)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {}
  [field-id field-props {:keys [disabled? on-click tab-indexed? tooltip-content] :as adornment-props}]
  ; BUG#2105
  ; An on-mouse-down event fired anywhere out of the input could trigger the
  ; on-blur event of the field. Therefore, the surface would dissapears unless
  ; if the on-mouse-down event prevented.
  ;
  ; If the user clicks on a field accessory (adornment, surface, placeholder, etc.)
  ; the field has get the focus!
  (-> {:class                 :pe-text-field--adornment
       :data-selectable       false
       :data-tooltip-content  (metamorphic-content/compose tooltip-content)
       :data-tooltip-position :left
       :on-mouse-down (fn [e] (.preventDefault e)
                              (when (input.env/input-focused? field-id)
                                    (r/dispatch-fx [:pretty-elements.plain-field/focus-field! field-id])))}
      (merge (if disabled?        {:disabled   "1" :data-disabled true :data-cursor :default})
             (if-not tab-indexed? {:tab-index "-1"})
             (if-not disabled?    {:on-mouse-up #(pretty-build-kit/dispatch-event-handler! on-click)}))
      (pretty-build-kit/color-attributes  adornment-props)
      (pretty-build-kit/effect-attributes adornment-props)
      (pretty-build-kit/font-attributes   adornment-props)
      (pretty-build-kit/icon-attributes   adornment-props)))

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
  ; HACK#9760 (source-code/cljs/pretty_elements/plain_field/utils.cljs)
  (-> {:class               :pe-text-field--placeholder
       :data-font-size      :xs
       :data-letter-spacing :auto
       :data-line-height    :text-block
       :data-selectable     false
       :data-text-overflow  :hidden}
      (pretty-build-kit/effect-attributes field-props)))

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
  {:class :pe-text-field--input-emphasize
   :style {:height (text-field.env/field-auto-height field-id field-props)}})

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
  (-> {:class               :pe-text-field--input-container
       :data-letter-spacing :auto
       :style               style}
      (pretty-build-kit/border-attributes field-props)
      (pretty-build-kit/font-attributes   field-props)
      (pretty-build-kit/indent-attributes field-props)
      (pretty-build-kit/marker-attributes field-props)))

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
  ; BUG#2105
  {:class :pe-text-field--adornments-placeholder
   :on-mouse-down #(r/dispatch-fx [:pretty-elements.plain-field/focus-field! field-id])})

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
         {:class :pe-text-field--surface}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-input-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autofill-name (keyword)(opt)
  ;  :date-from (string)(opt)
  ;  :date-to (string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :max-length (integer)(opt)
  ;  :type (keyword)(opt)
  ;   :email, :number, :password, :tel, :text}
  ;
  ; @return (map)
  ; {:auto-complete (keyword)
  ;  :max (string)
  ;  :max-length (integer)
  ;  :min (string)
  ;  :name (keyword)
  ;  :on-blur (function)
  ;  :on-focus (function)
  ;  :type (keyword)}
  [field-id {:keys [autofill-name date-from date-to disabled? max-length type] :as field-props}]
  ; The {:type :date} fields range could be set by the :min and :max properties.
  ;
  ; HACK#9760 (source-code/cljs/pretty_elements/plain_field/utils.cljs)
  ;
  ; BUG#6782
  ; - https://stackoverflow.com/questions/12374442/chrome-ignores-autocomplete-off
  ; - The "ignore autocomplete='off' (Autofill)" flag is set to enabled by default in chrome.
  ;   chrome://flags/#ignore-autocomplete-off-autofill
  ; - The Chrome browser ...
  ;   ... ignores the {:autocomplete "off"} setting,
  ;   ... ignores the {:autocomplete "new-*"} setting,
  ;   ... acknowledges the {:name ...} value.
  ; - By using randomly generated ':auto-complete' and ':name' values, the browser cannot
  ;   suggest values to the field.
  ; - If you want the browser to suggest values for the field, pass an understandable value
  ;   for the ':autofill-name' property (e.g., :phone-number)!
  (-> (plain-field.attributes/field-input-attributes field-id field-props)
      (merge {:class      :pe-text-field--input
              :max-length max-length
              :type       type}
             (if-not disabled? {:min           date-from
                                :max           date-to
                                :auto-complete (or autofill-name (random/generate-keyword))
                                :name          (or autofill-name (random/generate-keyword))
                                :on-blur       (fn [_] (r/dispatch [:pretty-elements.text-field/field-blurred field-id field-props]))
                                :on-focus      (fn [_] (r/dispatch [:pretty-elements.text-field/field-focused field-id field-props]))}))
      (pretty-build-kit/effect-attributes       field-props)
      (pretty-build-kit/element-size-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [disabled?] :as field-props}]
  (-> {:class        :pe-text-field
       :data-covered disabled?}
      (pretty-build-kit/class-attributes        field-props)
      (pretty-build-kit/outdent-attributes      field-props)
      (pretty-build-kit/state-attributes        field-props)
      (pretty-build-kit/wrapper-size-attributes field-props)))
