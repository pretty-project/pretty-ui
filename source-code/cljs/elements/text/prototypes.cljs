
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text.prototypes
    (:require [candy.api        :refer [param]]
              [x.components.api :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  ;  :selectable? (boolean)}
  [{:keys [content] :as text-props}]
  (merge {:font-size        :s
          :font-weight      :normal
          :horizontal-align :left
          :line-height      :normal
          :selectable?      true}
         (param text-props)
         ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
         {:content (x.components/content content)}))
