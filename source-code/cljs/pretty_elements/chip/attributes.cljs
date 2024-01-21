
(ns pretty-elements.chip.attributes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-content-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pe-chip--content
   :data-font-size      :xs
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-overflow  :hidden})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn primary-button-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:border-radius (map)(opt)
  ;   {:all (keyword)(opt)
  ;    :tl (keyword)(opt)}
  ;  :disabled? (boolean)(opt)
  ;  :primary-button (map)
  ;   {:on-click-f (function)}}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [_ {{:keys [all tl]} :border-radius {:keys [on-click-f]} :primary-button :keys [disabled?]}]
  (if disabled? {:class             :pe-chip--primary-button
                 :disabled          true
                 :style {"--adaptive-border-radius" (pretty-build-kit/adaptive-border-radius (or all tl) 0.9)}}
                {:class             :pe-chip--primary-button
                 :data-click-effect :opacity
                 :on-click          on-click-f
                 :on-mouse-up       dom/blur-active-element!
                 :style {"--adaptive-border-radius" (pretty-build-kit/adaptive-border-radius (or all tl) 0.9)}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-body-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [chip-id chip-props]
  (-> {:class :pe-chip--body}
      (pretty-build-kit/border-attributes           chip-props)
      (pretty-build-kit/color-attributes            chip-props)
      (pretty-build-kit/effect-attributes           chip-props)
      (pretty-build-kit/element-min-size-attributes chip-props)
      (pretty-build-kit/element-size-attributes     chip-props)
      (pretty-build-kit/indent-attributes           chip-props)
      (pretty-build-kit/style-attributes            chip-props)
      (pretty-build-kit/unselectable-attributes     chip-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ chip-props]
  (-> {:class :pe-chip}
      (pretty-build-kit/class-attributes        chip-props)
      (pretty-build-kit/outdent-attributes      chip-props)
      (pretty-build-kit/state-attributes        chip-props)
      (pretty-build-kit/wrapper-size-attributes chip-props)))
