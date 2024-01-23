
(ns pretty-inputs.select.attributes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]
              [pretty-inputs.core.env :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]))

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
  (let [option-selected? (core.env/option-selected? select-id select-props option)]
       {:class               :pi-select--option-label
        :data-font-size      font-size
        :data-font-weight    (if option-selected? :semi-bold :medium)
        :data-letter-spacing :auto
        :data-line-height    :text-block}))

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
  (let [option-selected? (core.env/option-selected? select-id select-props option)
        option-color     (core.env/get-option-color select-id select-props option)
        on-click-f       (fn [_] (core.side-effects/select-option! select-id select-props option))]
       (-> {:class         :pi-select--option
            :data-selected option-selected?
            :disabled      disabled?}
           (merge (if-not option-color-f {:data-hover-color :highlight}))
           (pretty-build-kit/effect-attributes select-props)
           (pretty-build-kit/color-attributes       {:fill-color option-color})
           (pretty-build-kit/mouse-event-attributes {:on-click-f on-click-f :on-mouse-up-f dom/blur-active-element!}))))

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
      (pretty-build-kit/indent-attributes       select-props)
      (pretty-build-kit/style-attributes        select-props)
      (pretty-build-kit/unselectable-attributes select-props)))

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
      (pretty-build-kit/class-attributes   select-props)
      (pretty-build-kit/outdent-attributes select-props)
      (pretty-build-kit/state-attributes   select-props)))
