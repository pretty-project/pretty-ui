
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.09
; Description:
; Version: v1.2.0
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.css-handler
    (:require [app-fruits.dom    :as dom]
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r cache-control-uri]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def EXTERNAL-CSS-URI-BASE "/css/x/")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-style-elements
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ;
  ; @return (vector)
  [head-element]
  (vec (array-seq (.querySelectorAll head-element "[type=\"text/css\"]"))))

(defn- source-exists?
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

(defn- create-link-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filepath
  [filepath]
  (let [link-element (dom/create-element! "LINK")]
       (set! (.-href link-element) filepath)
       (set! (.-type link-element) "text/css")
       (set! (.-rel  link-element) "stylesheet")
       (return link-element)))

(defn- insert-link-element-as-first!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (DOM-element) link-element
  [head-element link-element]
  (dom/insert-as-first-of-query-selected! head-element link-element "[type=\"text/css\"]"))

(defn- insert-link-element-as-last!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (DOM-element) link-element
  [head-element link-element]
  (dom/insert-as-last-of-query-selected! head-element link-element "[type=\"text/css\"]"))

(defn- insert-link-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-element) head-element
  ; @param (DOM-element) link-element
  ; @param (map) context-props
  ;  {:as-first? (boolean)}
  [head-element link-element {:keys [as-first?]}]
  (if as-first? (insert-link-element-as-first! head-element link-element)
                (insert-link-element-as-last!  head-element link-element)))

(defn- filename->external-css-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  ;
  ; @return (string)
  [filename]
  (str EXTERNAL-CSS-URI-BASE filename))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-external-css!
  ; @param (string) filepath
  ; @param (map)(opt) context-props
  ;  {:as-first? (boolean)}
  ;
  ; @usage
  ;  (environment/add-external-css! "/css/filename.css")
  [filepath context-props]
  (let [head-element (dom/get-head-element)
        app-build    (a/app-build)
        filepath     (cache-control-uri (string/starts-with! filepath "/") app-build)
        link-element (create-link-element! filepath)]
       (if-not (source-exists? head-element filepath)
               (insert-link-element! head-element link-element context-props))))

; @usage
;  {:environment/add-external-css! "/css/filename.css"}
;
; @usage
;  [:environment/add-external-css! "/css/filename.css"]
(a/reg-handled-fx :environment/add-external-css! add-external-css!)

(defn add-css!
  ; @param (string) filename
  ; @param (map)(opt) context-props
  ;  {:as-first? (boolean)}
  ;
  ; @usage
  ;  (environment/add-css! "/filename.css")
  [filename context-props]
  (let [filepath (filename->external-css-uri filename)]
       (add-external-css! filepath context-props)))

; @usage
;  {:environment/add-css! "filename.css"}
;
; @usage
;  [:environment/add-css! "filename.css"]
(a/reg-handled-fx :environment/add-css! add-css!)

(defn remove-css!
  ; @param (string) filename
  ;
  ; @usage
  ;  (environment/remove-css! "/filename.css")
  [filename])
  ; TODO ...

; @usage
;  {:environment/remove-css! "/filename.css"}
;
; @usage
;  [:environment/remove-css! "/filename.css"]
(a/reg-handled-fx :environment/remove-css! remove-css!)
