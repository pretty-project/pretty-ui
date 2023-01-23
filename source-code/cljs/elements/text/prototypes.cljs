
(ns elements.text.prototypes
    (:require [x.components.api :as x.components]
              [noop.api         :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; @ignore
  ;
  ; @param (map) text-props
  ; {:content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:content (string)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :line-height (keyword)
  ;  :selectable? (boolean)
  ;  :text-overflow (keyword)}
  [{:keys [content] :as text-props}]
  (merge {:font-size        :s
          :font-weight      :normal
          :horizontal-align :left
          :line-height      :text-block
          :selectable?      true
          :text-overflow    :wrap}
         (param text-props)
         ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
         {:content (x.components/content content)}))
