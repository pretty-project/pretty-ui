
(ns pretty-elements.text.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api    :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; @ignore
  ;
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :line-height (keyword, px or string)
  ;  :placeholder (metamorphic-content)
  ;  :text-overflow (keyword)}
  [text-props]
  ; BUG#9811 (source-code/cljs/pretty_elements/label/views.cljs)
  (merge {:font-size     :s
          :font-weight   :normal
          :line-height   :text-block
          :placeholder   "\u00A0"
          :text-overflow :wrap}
         (-> text-props)))
