
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.css-handler.helpers
    (:require [candy.api         :refer [return]]
              [dom.api           :as dom]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-style-elements
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ;
  ; @return (vector)
  [head-element]
  (-> (.querySelectorAll head-element "[type=\"text/css\"]") array-seq vec))

(defn source-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (string) uri
  ;
  ; @return (boolean)
  [head-element uri]

  ; WARNING! NOT TESTED!
  (vector/any-item-match? (get-style-elements head-element)
                         #(let [href (.-href %1)] (string/ends-with? href uri))))

(defn create-link-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) uri
  [uri]
  (let [link-element (dom/create-element! "LINK")]
       (as->   link-element % (set! (.-href %) uri)
                              (set! (.-type %) "text/css")
                              (set! (.-rel  %) "stylesheet"))
       (return link-element)))

(defn insert-link-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (DOM-element) link-element
  ; @param (map) options
  ;  {:as-first? (boolean)}
  [head-element link-element {:keys [as-first?]}]
  (if as-first? (dom/insert-as-first-of-query-selected! head-element link-element "[type=\"text/css\"]")
                (dom/insert-as-last-of-query-selected!  head-element link-element "[type=\"text/css\"]")))
