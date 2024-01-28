
(ns pretty-inputs.select.attributes
    (:require [dom.api                   :as dom]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.control.api    :as pretty-css.control]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api       :as pretty-css.live]
              [pretty-engine.api         :as pretty-engine]))

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
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _]
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
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [font-size] :as select-props} option]
  (let [option-selected? (pretty-engine/input-option-selected? select-id select-props option)]
       (-> {:class               :pi-select--option-label
            :data-font-size      font-size
            :data-font-weight    (if option-selected? :semi-bold :medium)
            :data-letter-spacing :auto
            :data-line-height    :text-block}
         (pretty-css.content/unselectable-text-attributes select-props))))

(defn select-option-checkmark-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _]
  {:class            :pi-select--option-checkmark
   :data-icon-family :material-symbols-outlined})

(defn select-option-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:disabled? (boolean)(opt)
  ;  :option-color-f (function)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [disabled? option-color-f] :as select-props} option]
  (let [option-selected? (pretty-engine/input-option-selected? select-id select-props option)
        option-color     (pretty-engine/get-input-option-color select-id select-props option)
        on-click-f       (fn [_] (pretty-engine/select-input-option! select-id select-props option))]
       (-> {:class         :pi-select--option
            :data-selected option-selected?
            :disabled      disabled?}
           (merge (if-not option-color-f {:data-hover-color :highlight}))
           (pretty-css.live/effect-attributes select-props)
           (pretty-css.appearance/background-attributes       {:fill-color option-color})
           (pretty-css.control/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

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
      (pretty-css.layout/indent-attributes select-props)
      (pretty-css.basic/style-attributes  select-props)))

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
      (pretty-css.basic/class-attributes   select-props)
      (pretty-css.layout/outdent-attributes select-props)
      (pretty-css.basic/state-attributes   select-props)
      (pretty-css.appearance/theme-attributes   select-props)))
