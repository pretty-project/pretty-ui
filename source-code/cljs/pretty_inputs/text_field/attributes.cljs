
(ns pretty-inputs.text-field.attributes
    (:require [dom.api                              :as dom]
              [fruits.random.api                    :as random]
              [metamorphic-content.api              :as metamorphic-content]
              [pretty-build-kit.api                 :as pretty-build-kit]
              [pretty-inputs.input.env              :as input.env]
              [pretty-inputs.plain-field.attributes :as plain-field.attributes]
              [pretty-inputs.text-field.env         :as text-field.env]
              [re-frame.api                         :as r]))

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
  ; @bug (#2105)
  ; An 'on-mouse-down' event fired anywhere out of the input could trigger the
  ; 'on-blur' event of the field. Therefore, the surface would dissapear unless
  ; the 'on-mouse-down' event is prevented.
  ;
  ; If the user clicks on any field accessory (adornment, surface, placeholder, etc.) the field must get focused!
  (-> {:class                 :pi-text-field--adornment
       :data-selectable       false
       :data-tooltip-content  (metamorphic-content/compose tooltip-content)
       :data-tooltip-position :left
       :on-mouse-down (fn [e] (dom/prevent-default e)
                              (when (input.env/input-focused? field-id)
                                    (r/dispatch-fx [:pretty-inputs.plain-field/focus-field! field-id])))}
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
  (-> {:class               :pi-text-field--placeholder
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
  {:class :pi-text-field--input-emphasize
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
  (-> {:class               :pi-text-field--input-container
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
  {:class :pi-text-field--adornments-placeholder
   :on-mouse-down #(r/dispatch-fx [:pretty-inputs.plain-field/focus-field! field-id])})

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
         {:class :pi-text-field--surface}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-input-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  (plain-field.attributes/field-input-attributes field-id field-props))

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
  (-> {:class        :pi-text-field
       :data-covered disabled?}
      (pretty-build-kit/class-attributes        field-props)
      (pretty-build-kit/outdent-attributes      field-props)
      (pretty-build-kit/state-attributes        field-props)
      (pretty-build-kit/wrapper-size-attributes field-props)))
