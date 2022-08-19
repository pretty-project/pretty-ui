

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.stick-observer)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; TEMP
; https://css-tricks.com/how-to-detect-when-a-sticky-element-gets-pinned/
(defn stick-observer
  [element]
  (letfn [(f [entries] (doseq [e entries]
                              (if (< (.-intersectionRatio e) 1)
                                  (.setAttribute    element "data-sticky" true)
                                  (.removeAttribute element "data-sticky"))))]
         (let [observer (js/IntersectionObserver. f (clj->js {:threshold [1]}))]
              (.observe observer observed-element))))
; TEMP
