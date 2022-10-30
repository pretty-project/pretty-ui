
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.content.views
    (:require [mid-fruits.candy             :refer [param return]]
              [mid-fruits.hiccup            :refer [hiccup?]]
              [mid-fruits.random            :as random]
              [mid-fruits.string            :as string]
              [reagent.api                  :refer [component?]]
              [re-frame.api                 :as r]
              [x.app-components.transmitter :rename {component transmitter}]
              [x.app-dictionary.api         :as x.dictionary]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- string-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:content (keyword)
  ;   :prefix (string)(opt)
  ;   :replacements (vector)(opt)
  ;   :suffix (string)(opt)}
  ;
  ; @return (string)
  [_ {:keys [content prefix replacements suffix]}]
  (if-not (empty? content)
          (if replacements (string/use-replacements (str prefix content suffix) replacements)
                           (str prefix content suffix))))

(defn- number-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (string)
  [component-id context-props]
  (string-content component-id (update context-props :content str)))

(defn- dictionary-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:content (keyword)}
  ;
  ; @return (string)
  [_ {:keys [content] :as context-props}]
  (x.dictionary/looked-up content context-props))

(defn- static-render-fn-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :content (function)}
  [component-id {:keys [base-props content]}]
  [transmitter component-id {:base-props base-props
                             :render-f   content}])

(defn- subscribed-render-fn-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :content (function)
  ;   :subscriber (subscription-vector)}
  [component-id {:keys [subscriber] :as context-props}]
  (let [subscribed-props (r/subscribe subscriber)]
       (fn [_ {:keys [base-props content]}]
           [transmitter component-id {:base-props       base-props
                                      :render-f         content
                                      :subscribed-props @subscribed-props}])))

(defn- render-fn-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:subscriber (subscription-vector)(opt)}
  [component-id {:keys [subscriber] :as context-props}]
  (if subscriber [subscribed-render-fn-content component-id context-props]
                 [static-render-fn-content     component-id context-props]))

(defn- content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:content (component, keyword, integer or string)}
  [component-id {:keys [content] :as context-props}]
  (cond (keyword? content) (dictionary-content component-id context-props)
        (string?  content) (string-content     component-id context-props)
        (number?  content) (number-content     component-id context-props)
      ; #' The symbol must resolve to a var, and the Var object itself (not its value) is returned.
      ;
      ; (var? #'my-component) => true
      ; (fn?  #'my-component) => true
      ; (var?   my-component) => false
      ; (fn?    my-component) => true
      ;
      ; (var?       content) [render-fn-content component-id context-props]
        (fn?        content) [render-fn-content component-id context-props]
      ; A cond feltétel-listájának :return ága kielégíti a (component? ...) és (hiccup? ...) feltételeket!
      ; (component? content) (return      content)
      ; (hiccup?    content) (return      content)
        :return     content))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:base-props (map)(opt)
  ;    W/ {:content (component)}
  ;   :content (component, function, keyword, hiccup, integer or string)(opt)
  ;   :prefix (string)(opt)
  ;   :replacements (vector)(opt)
  ;    W/ {:content (keyword or string)}
  ;   :subscriber (subscription-vector)(opt)
  ;    A visszatérési értéknek térkép típusnak kell lennie!
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
  ;  [content {:content :username}]
  ;  =>
  ;  "Username"
  ;
  ; @example
  ;  [content {:content "Hakuna Matata"}]
  ;  =>
  ;  "Hakuna Matata"
  ;
  ; @example
  ;  (defn my-component [component-id])
  ;  [content :my-component {:content #'my-component}]
  ;
  ; @example
  ;  (defn my-component [component-id])
  ;  [content {:content [my-component :my-component]}]
  ;
  ; @example
  ;  (defn my-component-a [component-id])
  ;  (defn my-component-b [component-id])
  ;  [content {:content [:<> [my-component-a :my-component]
  ;                          [my-component-b :your-component]]}]
  ;
  ; @example
  ;  (defn my-component [component-id component-props])
  ;  [content :my-component
  ;           {:content    #'my-component
  ;            :subscriber [:get-my-component-props]}]
  ;
  ; @example
  ;  (defn my-component [component-id component-props])
  ;  [content {:content    [my-component :my-component]
  ;            :subscriber [:get-my-component-props]}]
  ([context-props]
   (component (random/generate-keyword) context-props))

  ([component-id context-props]
   (if-not (map? context-props)
           (content component-id {:content context-props})
           (content component-id           context-props))))
