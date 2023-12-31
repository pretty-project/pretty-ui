
(ns pretty-elements.chip.attributes
    (:require [dom.api        :as dom]
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
  ;   {:on-click (function or Re-Frame metamorphic-event)}}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [_ {{:keys [all tl]} :border-radius {:keys [on-click]} :primary-button :keys [disabled?]}]
  (if disabled? {:class             :pe-chip--primary-button
                 :disabled          true
                 :style {"--adaptive-border-radius" (pretty-build-kit/adaptive-border-radius (or all tl) 0.9)}}
                {:class             :pe-chip--primary-button
                 :data-click-effect :opacity
                 :on-click          #(pretty-build-kit/dispatch-event-handler! on-click)
                 :on-mouse-up       #(dom/blur-active-element!)
                 :style {"--adaptive-border-radius" (pretty-build-kit/adaptive-border-radius (or all tl) 0.9)}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-body-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [chip-id {:keys [on-click style] :as chip-props}]

  (-> (if on-click {:class             :pe-chip--body
                    :data-click-effect :opacity
                    :data-selectable   false
                    :style             style}
                   {:class             :pe-chip--body
                    :data-selectable   false
                    :style             style})
      (pretty-build-kit/border-attributes           chip-props)
      (pretty-build-kit/color-attributes            chip-props)
      (pretty-build-kit/element-min-size-attributes chip-props)
      (pretty-build-kit/element-size-attributes     chip-props)
      (pretty-build-kit/indent-attributes           chip-props)))

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
