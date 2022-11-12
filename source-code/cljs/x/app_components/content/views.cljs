
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.content.views
    (:require [candy.api            :refer [return]]
              [hiccup.api           :refer [hiccup?]]
              [mid-fruits.random    :as random]
              [mid-fruits.vector    :as vector]
              [reagent.api          :refer [component?]]
              [re-frame.api         :as r]
              [x.app-dictionary.api :as x.dictionary]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- string-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) content-id
  ; @param (map) content-props
  ;  {:content (keyword)
  ;   :prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;   :suffix (string)(opt)}
  ;
  ; @return (string)
  [_ {:keys [content prefix replacements suffix]}]
  (x.dictionary/join-string content prefix suffix replacements))

(defn- number-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) content-id
  ; @param (map) content-props
  ;
  ; @return (string)
  [content-id content-props]
  (string-content content-id (update content-props :content str)))

(defn- dictionary-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) content-id
  ; @param (map) content-props
  ;  {:content (keyword)}
  ;
  ; @return (string)
  [content-id {:keys [content] :as content-props}]
  (let [content @(r/subscribe [:dictionary/look-up content])]
       (string-content content-id (assoc content-props :content content))))

(defn- render-fn-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) content-id
  ; @param (map) content-props
  ;  {:content (function)
  ;   :params (vector)(opt)}
  [content-id {:keys [content params]}]
  (if params (vector/concat-items [content content-id] params)
             [content content-id]))

(defn- hiccup-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) content-id
  ; @param (map) content-props
  ;  {:content (hiccup)}
  [content-id {:keys [content]}]
  (return content))

(defn- component-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) content-id
  ; @param (map) content-props
  ;  {:content (component)
  ;   :params (vector)(opt)}
  [content-id {:keys [content params]}]
  (if params (vector/concat-items content params)
             (return              content)))

(defn- metamorphic-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) content-id
  ; @param (map) content-props
  ;  {:content (component, function, keyword, hiccup, integer or string)}
  [content-id {:keys [content] :as content-props}]
  ; #' The symbol must resolve to a var, and the Var object itself (not its value) is returned.
  ;
  ; (var? #'my-component) => true
  ; (fn?  #'my-component) => true
  ; (var?   my-component) => false
  ; (fn?    my-component) => true
  (cond (keyword?   content) (dictionary-content content-id content-props)
        (string?    content) (string-content     content-id content-props)
        (number?    content) (number-content     content-id content-props)
      ; (var?       content) [render-fn-content  content-id content-props]
        (fn?        content) [render-fn-content  content-id content-props]
        (component? content) [component-content  content-id content-props]
        (hiccup?    content) [hiccup-content     content-id content-props]
        :return     content))

(defn component
  ; @param (keyword)(opt) content-id
  ; @param (metamorphic-content) content
  ;  {:content (component, function, keyword, hiccup, integer, number or string)(opt)
  ;   :params (vector)(opt)
  ;   :prefix (string)(opt)
  ;    W/ {:content (keyword or string)}
  ;   :replacements (numbers or strings in vector)(opt)
  ;    W/ {:content (keyword or string)}
  ;   :suffix (string)(opt)
  ;    W/ {:content (keyword or string)}}
  ;
  ; @usage
  ;  [content {...}]
  ;
  ; @usage
  ;  [content :my-component {...}]
  ;
  ; @example
  ;  [content {:content :first-name}]
  ;  =>
  ;  "First name"
  ;
  ; @example
  ;  [content {:content "Hakuna Matata"}]
  ;  =>
  ;  "Hakuna Matata"
  ;
  ; @usage
  ;  (defn my-component [content-id])
  ;  [content :my-content {:content #'my-component}]
  ;
  ; @usage
  ;  (defn my-component [my-color])
  ;  [content {:content [my-component :green]}]
  ;
  ; @usage
  ;  (defn my-component   [my-color])
  ;  (defn your-component [your-color])
  ;  [content {:content [:<> [my-component   :green]
  ;                          [your-component :blue]]}]
  ;
  ; @usage
  ;  (defn my-component [content-id my-color])
  ;  [content :my-content {:content #'my-component
  ;                        :params  [:green]}]
  ;
  ; @usage
  ;  (defn my-component [content-id my-color])
  ;  [content {:content [my-component :my-component]
  ;            :params  [:green]}]
  ([content]
   (component (random/generate-keyword) content))

  ([content-id content]
   (if (map?    content)
       (metamorphic-content content-id           content)
       (metamorphic-content content-id {:content content}))))
