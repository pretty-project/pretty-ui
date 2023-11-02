
(ns elements.chip.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-label-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pe-chip--label
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
  ; {:disabled? (boolean)(opt)
  ;  :primary-button (map)
  ;   {:on-click (Re-Frame metamorphic-event)}}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [_ {{:keys [on-click]} :primary-button :keys [disabled?]}]
  (if disabled? {:class             :pe-chip--primary-button
                 :disabled          true}
                {:class             :pe-chip--primary-button
                 :data-click-effect :opacity
                 :on-click          #(r/dispatch on-click)
                 :on-mouse-up       #(dom/blur-active-element!)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-body-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:on-click (Re-Frame metamorphic-event)(opt)
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
      (pretty-css/color-attributes  chip-props)
      (pretty-css/indent-attributes chip-props)))

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
      (pretty-css/default-attributes          chip-props)
      (pretty-css/outdent-attributes          chip-props)
      (pretty-css/element-min-size-attributes chip-props)
      (pretty-css/element-size-attributes     chip-props)))
