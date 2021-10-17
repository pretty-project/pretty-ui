
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.09
; Description:
; Version: v0.9.8
; Compatibility: x3.9.9



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
  (dom/insert-as-last-of-query-selected!  head-element link-element "[type=\"text/css\"]"))

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



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

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

(defn- add-external-source!
  ; @param (string) filepath
  ; @param (map)(opt) context-props
  ;  {:as-first? (boolean)}
  [filepath context-props]
  (let [head-element (dom/get-head-element)
        app-build    (a/subscribed [:x.app-core/get-app-detail :app-build])
        filepath     (cache-control-uri (string/starts-with! filepath "/")
                                        (param app-build))
        link-element (create-link-element! filepath)]
       (if-not (source-exists? head-element filepath)
               (insert-link-element! head-element link-element context-props))))

(a/reg-handled-fx
  ::add-source!
  ; @param (string) filename
  ;  "filename.css"
  ; @param (map)(opt) context-props
  ;  {:as-first? (boolean)}
  ;
  ; @usage
  ;  [:x.app-environment.css-handler/add-source! "filename.css"]
  (fn [[filename context-props]]
      (add-external-source! (filename->external-css-uri filename) context-props)))

(a/reg-handled-fx
  ::add-external-source!
  ; @param (string) filepath
  ; @param (map)(opt) context-props
  ;  {:as-first? (boolean)}
  ;
  ; @usage
  ;  [:x.app-environment.css-handler/add-external-source!
  ;   "/directory-name/filename.css"]
  (fn [[filepath context-props]]
      (add-external-source! filepath context-props)))

(a/reg-handled-fx
  ::remove-source!
  ; @param (string) filename
  ;
  ; @usage
  ;  [:x.app-environment.css-handler/remove-source! "/my-style.css"]
  (fn [[filename]]))
  ; TODO
