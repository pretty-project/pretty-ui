
(ns pretty-elements.text.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [_ text-props]
  ; @bug (pretty-elements.label.prototypes#9811)
  (-> text-props (pretty-properties/default-content-props      {:content-placeholder "\u00A0"})
                 (pretty-properties/default-flex-props         {:orientation :horizontal})
                 (pretty-properties/default-font-props         {:font-size :s :font-weight :medium})
                 (pretty-properties/default-size-props         {:size-unit :full-block})
                 (pretty-properties/default-text-props         {:text-selectable? true})
                 (pretty-standards/standard-border-props)
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-font-props)
                 (pretty-standards/standard-text-props)
                 (pretty-standards/standard-wrapper-size-props)
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
                 (pretty-rules/apply-auto-border-crop)
                 (pretty-rules/compose-content)
                 (pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/auto-adapt-wrapper-size)))



  ; BUG#9811 (source-code/cljs/pretty_elements/label/views.cljs)
  ;(merge {:font-size     :s
  ;        :font-weight   :normal
  ;        :line-height   :text-block
  ;        :content-placeholder   "\u00A0"
  ;        :text-overflow :wrap
  ;       (-> text-props)))
         ;(pretty-properties/default-size-props {:size-unit :full-block})))
         ;(pretty-properties/default-wrapper-size-props {})))
