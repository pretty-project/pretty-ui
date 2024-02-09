
(ns pretty-elements.text.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :line-height (keyword, px or string)
  ;  :content-placeholder (metamorphic-content)
  ;  :text-overflow (keyword)}
  [_ text-props]
  ; BUG#9811 (source-code/cljs/pretty_elements/label/views.cljs)
  (merge {:font-size     :s
          :font-weight   :normal
          :line-height   :text-block
          :content-placeholder   "\u00A0"
          :text-overflow :wrap}
         (-> text-props)))
         ;(pretty-properties/default-size-props {:size-unit :full-block})))
         ;(pretty-properties/default-wrapper-size-props {})))
