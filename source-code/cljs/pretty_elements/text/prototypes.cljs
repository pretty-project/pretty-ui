
(ns pretty-elements.text.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; @ignore
  ;
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :line-height (keyword)
  ;  :placeholder (metamorphic-content)
  ;  :selectable? (boolean)
  ;  :text-overflow (keyword)
  ;  :width (keyword)}
  [text-props]
  ; BUG#9811 (source-code/cljs/pretty_elements/label/views.cljs)
  (merge {:font-size        :s
          :font-weight      :normal
          :horizontal-align :left
          :line-height      :text-block
          :placeholder      "\u00A0"
          :selectable?      true
          :text-overflow    :wrap
          :width            :content}
         (-> text-props)))
