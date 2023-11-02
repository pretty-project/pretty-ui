
(ns pretty-elements.card.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-body-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [card-id {:keys [disabled? horizontal-align hover-effect href on-click style] :as card-props}]
  (-> (if disabled? {:class                        :pe-card--body
                     :data-horizontal-column-align horizontal-align
                     :disabled                     true
                     :style                        style}
                    {:class                        :pe-card--body
                     :data-click-effect            (if (or href on-click) :opacity)
                     :data-hover-effect            hover-effect
                     :data-horizontal-column-align horizontal-align
                     :style                        style
                     :on-click                     #(r/dispatch on-click)})

                    ; BUG#9810
                    ; A card elemben elhelyezett text-field mezőre kattintáskor,
                    ; az on-mouse-up esemény elvette a fókuszt a mezőről!
                    ; Miért került rá ez a kártyákra?
                    ; :on-mouse-up #(dom/blur-active-element!)

      (pretty-css/badge-attributes  card-props)
      (pretty-css/border-attributes card-props)
      (pretty-css/color-attributes  card-props)
      (pretty-css/cursor-attributes card-props)
      (pretty-css/indent-attributes card-props)
      (pretty-css/link-attributes   card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ card-props]
  (-> {:class :pe-card}
      (pretty-css/default-attributes          card-props)
      (pretty-css/outdent-attributes          card-props)
      (pretty-css/element-max-size-attributes card-props)
      (pretty-css/element-min-size-attributes card-props)
      (pretty-css/element-size-attributes     card-props)))
