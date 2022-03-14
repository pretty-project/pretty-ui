
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.css-handler.helpers
    (:require [app-fruits.dom                       :as dom]
              [mid-fruits.candy                     :refer [param return]]
              [mid-fruits.string                    :as string]
              [mid-fruits.vector                    :as vector]
              [x.app-environment.css-handler.config :as css-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-style-elements
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ;
  ; @return (vector)
  [head-element]
  (vec (array-seq (.querySelectorAll head-element "[type=\"text/css\"]"))))

(defn source-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (string) filepath
  ;
  ; @return (boolean)
  [head-element filepath]

  ; WARNING! NOT TESTED!
  (vector/any-item-match? (get-style-elements head-element)
                         #(let [href (.-href %1)]
                               (string/ends-with? href filepath))))

(defn create-link-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filepath
  [filepath]
  (let [link-element (dom/create-element! "LINK")]
       (as->   link-element % (set! (.-href %) filepath)
                              (set! (.-type %) "text/css")
                              (set! (.-rel  %) "stylesheet"))
       (return link-element)))

(defn insert-link-element-as-first!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (DOM-element) link-element
  [head-element link-element]
  (dom/insert-as-first-of-query-selected! head-element link-element "[type=\"text/css\"]"))

(defn insert-link-element-as-last!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (DOM-element) link-element
  [head-element link-element]
  (dom/insert-as-last-of-query-selected! head-element link-element "[type=\"text/css\"]"))

(defn insert-link-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (DOM-element) link-element
  ; @param (map) context-props
  ;  {:as-first? (boolean)}
  [head-element link-element {:keys [as-first?]}]
  (if as-first? (insert-link-element-as-first! head-element link-element)
                (insert-link-element-as-last!  head-element link-element)))

(defn filename->external-css-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  ;
  ; @return (string)
  [filename]
  (str css-handler.config/EXTERNAL-CSS-URI-BASE filename))
