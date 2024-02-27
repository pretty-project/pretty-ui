
(ns pretty-inputs.option.attributes
    (:require [dom.api                  :as dom]
              [pretty-attributes.api    :as pretty-attributes]
              [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-options-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-checkbox--options-placeholder
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
      (pretty-attributes/text-attributes checkbox-props)))
      ; + :text-selectable? false

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
           (pretty-attributes/background-color-attributes {:fill-color option-color})
           (pretty-attributes/border-attributes checkbox-props))))

(defn option-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  [_ option-props]
  (-> {:class :pi-option--icon}

      (pretty-attributes/icon-attributes option-props)))

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
           (pretty-attributes/effect-attributes checkbox-props)
           (pretty-attributes/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) option-id
  ; @param (map) option-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ option-props]
  (-> {:class :pi-option--inner}
      (pretty-attributes/flex-attributes        option-props)
      (pretty-attributes/inner-size-attributes  option-props)
      (pretty-attributes/inner-space-attributes option-props)
      (pretty-attributes/style-attributes       option-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-attributes
  ; @ignore
  ;
  ; @param (keyword) option-id
  ; @param (map) option-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ option-props]
  (-> {:class :pi-option}
      (pretty-attributes/class-attributes          checkbox-props)
      (pretty-attributes/inner-position-attributes checkbox-props)
      (pretty-attributes/outer-position-attributes checkbox-props)
      (pretty-attributes/outer-size-attributes     checkbox-props)
      (pretty-attributes/outer-space-attributes    checkbox-props)
      (pretty-attributes/state-attributes          checkbox-props)
      (pretty-attributes/theme-attributes          checkbox-props)))