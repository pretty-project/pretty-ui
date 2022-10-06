
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.pdf-generator.helpers
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.css    :as css]
              [mid-fruits.hiccup :as hiccup]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn parse-css
  ; @param (*) n
  ;
  ; @example
  ;  (pdf-generator/parse-css [:td [:p {:style {:color "red"}}]])
  ;  =>
  ;  [:td [:p {:style "color: red;"}]]
  ;
  ; @example
  ;  (pdf-generator/parse-css [:td [:p {:style "color: red;"}]])
  ;  =>
  ;  [:td [:p {:style "color: red;"}]]
  ;
  ; @return (*)
  [n]
  (letfn [(f [n] (let [style (hiccup/get-style n)]
                      (if (map? style)
                          (hiccup/set-style n (css/parse style))
                          (return           n))))]
         (hiccup/walk n f)))
