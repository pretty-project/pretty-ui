
(ns pretty-inputs.checkbox.attributes
    (:require [dom.api                   :as dom]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.control.api    :as pretty-css.control]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api       :as pretty-css.live]
              [pretty-inputs.engine.api         :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-checkbox--placeholder
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-color     :highlight})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _ _]
  {:class               :pi-checkbox--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto
   :data-text-color     :muted})

(defn checkbox-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size] :as checkbox-props} _ _]
  (-> {:class               :pi-checkbox--option-label
       :data-font-size      font-size
       :data-font-weight    :medium
       :data-letter-spacing :auto
       :data-line-height    :text-block}
      (pretty-css.content/unselectable-text-attributes checkbox-props)))

(defn checkbox-option-button-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [checkbox-id checkbox-props option-dex option]
  (let [option-color (pretty-inputs.engine/get-input-option-color checkbox-id checkbox-props option-dex option)]
       (-> {:class :pi-checkbox--option-button}
           (pretty-css.appearance/background-attributes {:fill-color option-color})
           (pretty-css.appearance/border-attributes checkbox-props))))

(defn checkbox-option-checkmark-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _ _]
  {:class            :pi-checkbox--option-checkmark
   :data-icon-family :material-symbols-outlined})

(defn checkbox-option-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-checked (boolean)
  ;  :disabled (boolean)}
  [checkbox-id {:keys [disabled?] :as checkbox-props} option-dex option]
  (let [option-selected? (pretty-inputs.engine/input-option-selected? checkbox-id checkbox-props option-dex option)
        on-click-f       (fn [_] (pretty-inputs.engine/select-input-option! checkbox-id checkbox-props option-dex option))]
       (-> {:class        :pi-checkbox--option
            :data-checked option-selected?
            :disabled     disabled?}
           (pretty-css.live/effect-attributes checkbox-props)
           (pretty-css.control/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-body-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :on-blur (function)
  ;  :on-focus (function)}
  [checkbox-id checkbox-props]
  (let [on-blur-f  (fn [_] (pretty-inputs.engine/input-left    checkbox-id checkbox-props))
        on-focus-f (fn [_] (pretty-inputs.engine/input-focused checkbox-id checkbox-props))]
       (-> {:class    :pi-checkbox--body
            :on-blur  on-blur-f
            :on-focus on-focus-f}
           (pretty-css.layout/indent-attributes      checkbox-props)
           (pretty-css.layout/flex-attributes checkbox-props)
           (pretty-css.basic/style-attributes       checkbox-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ checkbox-props]
  (-> {:class :pi-checkbox}
      (pretty-css.basic/class-attributes   checkbox-props)
      (pretty-css.layout/outdent-attributes checkbox-props)
      (pretty-css.basic/state-attributes   checkbox-props)
      (pretty-css.appearance/theme-attributes   checkbox-props)))
