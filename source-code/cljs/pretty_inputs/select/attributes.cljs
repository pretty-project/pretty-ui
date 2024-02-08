
(ns pretty-inputs.select.attributes
    (:require [dom.api                  :as dom]
              [pretty-attributes.api    :as pretty-attributes]
              [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-select--placeholder
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-color     :highlight})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _ _]
  {:class               :pi-select--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto
   :data-text-color     :muted})

(defn select-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [font-size] :as select-props} option-dex option]
  (let [option-selected? (pretty-inputs.engine/input-option-selected? select-id select-props option-dex option)]
       (-> {:class               :pi-select--option-label
            :data-font-size      font-size
            :data-font-weight    (if option-selected? :semi-bold :medium)
            :data-letter-spacing :auto
            :data-line-height    :text-block}
         (pretty-attributes/text-attributes select-props))))
         ; + :text-selectable? false

(defn select-option-checkmark-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _ _]
  {:class            :pi-select--option-checkmark
   :data-icon-family :material-symbols-outlined})

(defn select-option-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:disabled? (boolean)(opt)
  ;  :option-color-f (function)(opt)}
  ; @param (integer) option-dex
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [disabled? option-color-f] :as select-props} option-dex option]
  (let [option-selected? (pretty-inputs.engine/input-option-selected? select-id select-props option-dex option)
        option-color     (pretty-inputs.engine/get-input-option-color select-id select-props option-dex option)
        on-click-f       (fn [_] (pretty-inputs.engine/select-input-option! select-id select-props option-dex option))]
       (-> {:class         :pi-select--option
            :data-selected option-selected?
            :disabled      disabled?}
           (merge (if-not option-color-f {:data-hover-color :highlight}))
           (pretty-attributes/effect-attributes select-props)
           (pretty-attributes/background-color-attributes       {:fill-color option-color})
           (pretty-attributes/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-options-label-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  [_ _]
  {:class            :pi-select--options-label
   :data-font-size   :s
   :data-font-weight :medium
   :data-line-height :text-block})

   ; nem kell ra unselectable?

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-body-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [_ select-props]
  (-> {:class :pi-select--body}
      (pretty-attributes/indent-attributes select-props)
      (pretty-attributes/style-attributes  select-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [_ select-props]
  (-> {:class :pi-select}
      (pretty-attributes/class-attributes  select-props)
      (pretty-attributes/outdent-attributes select-props)
      (pretty-attributes/state-attributes  select-props)
      (pretty-attributes/theme-attributes   select-props)))
