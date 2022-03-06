
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.html.views
    (:require [hiccup.page      :refer [html5]]
              [mid-fruits.candy :refer [param]]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (hiccup) head
  ; @param (hiccup) body
  ;
  ; @usage
  ;  (ui/html (ui/head) (ui/body))
  ;
  ; @usage
  ;  (ui/html [:head] [:body])
  [head body]
  (html5 {:id    "x-document-element"
          ; XXX#7659
          :style "overflow-y: hidden"}
         (param head)
         (param body)))
