
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v2.2.4
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.content
    (:require [mid-fruits.candy             :refer [param return]]
              [mid-fruits.map               :as map]
              [mid-fruits.string            :as string]
              [mid-fruits.vector            :as vector]
              [x.app-components.transmitter :rename {component transmitter}]
              [x.app-core.api               :as a :refer [r]]
              [x.app-dictionary.api         :as dictionary]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#8711 ----------------------------------------------------------------

; @name content
;  A (metamorphic-content) típust a content komponens {:content ...} tulajdonsága
;  valósítja meg.
;  - Értéke lehet szimbólumként átadott komponens:
;    {:content #'my-component}
;  - Értéke lehet React komponensként átadott komponens:
;    {:content [:<> [my-component] ...]}
;  - Értéke lehet az app-dictionary szótár egy kifejezésére utaló kulcsszó:
;    {:content :my-term}
;  - Értéke lehet egy többnyelvő térkép, amely a felhasználói felületen
;    kiválasztott nyelv szerinti kulcshoz tartózó értékként adódik át:
;    {:content {:en [:data :in :english] :hu [:adatok :magyarul]}}
;  - Értéke lehet egy egyszerű string:
;    {:content "My content"}
;  - Értéke lehet egy hiccup vektor:
;    {:content [:div "My content"]}
;
; @name base-props
;  Az [x.app-components/content] komponens számára {:content ...} tulajdonságként
;  átadott komponens második paramétereként átadott térkép alapja
;  (az XXX#0001 logika szerint)
;
; @name content-props
;  Ha a content komponensnek {:content ...} tulajdonságként egy komponens kerül
;  átadásra, akkor ennek az átadott komponensnek második paraméterként adódik
;  át a {:content-props {...}} térkép.
;
; @name subscriber
;  Ha a content komponensnek {:content ...} tulajdonságként egy komponens kerül
;  átadásra, akkor a {:subscriber [...]} tulajdonságként átadott Re-Frame
;  subscription vektor használatával a content komponens feliratkozik
;  a subscription visszatérési értékére, és azt ha nincs megadva {:content-props {...}}
;  tulajdonság, akkor második paraméterként, ha van megadva {:content-props {...}}
;  tulajdonság, akkor harmadik paraméterként a {:content ...} tulajdonságként
;  átadott komponens számára átadja.
;
;  Ha a content komponensnek {:content ...} tulajdonság nem kerül átadásra,
;  akkor a {:subscriber [...]} tulajdonságként átadott Re-Frame
;  subscription vektor használatával a content komponens feliratkozik
;  a subscription visszatérési értékére, és azt string-ként kiértékelve a content
;  komponens {:content ...} tulajdonságaként használja.
;
; @name suffix
;  Ha a content komponensnek {:content ...} tulajdonságként az app-dictionary
;  szótár egy kifejezésre utaló kulcszó vagy egy többnyelvű térkép kerül átadásra,
;  akkor a {:suffix ...} tulajdonságként átadott szöveges tartalmat toldalékaként
;  használja.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extended-props->content-props
  ; @param (map) extended-props
  ;
  ; @return (map)
  [extended-props]
  (map/inherit (param extended-props)
               [:base-props :content :content-props :prefix :replacements :subscriber :suffix]))

(defn- context-props->subscribe?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:suffix (string)(opt)}
  ;
  ; @return (boolean)
  [{:keys [subscriber]}]
  (some? subscriber))



;; -- Components --------------------------------------------------------------
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
  (string/use-replacements (str prefix content suffix) replacements))

(defn- dictionary-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:content (keyword)
  ;   :replacements (vector)(opt)
  ;   :suffix (string)(opt)}
  ;
  ; @return (string)
  [_ {:keys [content] :as context-props}]
  (dictionary/looked-up content context-props))

(defn- multilingual-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:content (map)
  ;   :suffix (string)(opt)}
  ;
  ; @return (string)
  [_ {:keys [content suffix]}]
  (str (dictionary/translated content) suffix))

(defn- nil-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:subscriber (subscription vector)(opt)}
  ;
  ; @return (string)
  [_ {:keys [subscriber]}]
  (if (some? subscriber)
      (let [content (a/subscribe subscriber)]
           (fn [] (str @content)))))

(defn- static-component-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :content (component)
  ;   :content-props (map)(opt)}
  ;
  ; @return (component)
  [component-id {:keys [base-props content content-props]}]
  [transmitter component-id {:base-props   base-props
                             :component    content
                             :static-props content-props}])

(defn- subscribed-component-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :content (component)
  ;   :content-props (map)(opt)
  ;   :subscriber (subscription vector)}
  ;
  ; @return (component)
  [component-id {:keys [subscriber] :as context-props}]
  (let [subscribed-props (a/subscribe subscriber)]
       (fn [_ {:keys [base-props content content-props]}]
           [transmitter component-id {:base-props        base-props
                                      :component         content
                                      :static-props      content-props
                                      :subscribed-props @subscribed-props}])))

(defn- component-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:content (component)
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @return (component)
  [component-id context-props]
  (if (context-props->subscribe? context-props)
      [subscribed-component-content component-id context-props]
      [static-component-content     component-id context-props]))

(defn- content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:content (component, keyword or string)}
  ;
  ; @return (component or string)
  [component-id {:keys [content] :as context-props}]
  (cond (keyword? content) (dictionary-content   component-id context-props)
        (var?     content) [component-content    component-id context-props]
        (string?  content) (string-content       component-id context-props)
        (map?     content) (multilingual-content component-id context-props)
        (nil?     content) (nil-content          component-id context-props)
        :else              (return content)))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  XXX#8711
  ;  {:base-props (map)(opt)
  ;    Only w/ {:content (component)}
  ;   :content (component, keyword, hiccup, map or string)(opt)
  ;   :content-props (map)(opt)
  ;    Only w/ {:content (component)}
  ;   :prefix (string)(opt)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;    Only w/ {:content (keyword or string)}
  ;   :subscriber (subscription vector)(opt)
  ;    Return value must be a map!
  ;    Only w/ {:content (component)
  ;             :content (nil)}
  ;   :suffix (string)(opt)
  ;    Only w/ {:content (keyword or string)}}
  ;
  ; @usage
  ;  [components/content {...}]
  ;
  ; @usage
  ;  [components/content :my-component {...}]
  ;
  ; @example (dictionary term as keyword)
  ;  [components/content {:content :username}]
  ;  =>
  ;  "Username"
  ;
  ; @example (string)
  ;  [components/content {:content "Hakuna Matata"}]
  ;  =>
  ;  "Hakuna Matata"
  ;
  ; @example (multilingual-item as map)
  ;  [components/content {:content {:en "Window" :hu "Ablak"}}]
  ;  =>
  ;  "Window"
  ;
  ; @example (component)
  ;  (defn my-component [component-id])
  ;  [components/content {:content #'my-component}]
  ;
  ; @example (component)
  ;  (defn my-component [component-id view-props])
  ;  [components/content {:content    #'my-component
  ;                       :subscriber [::get-view-props]}]
  ;
  ; @example (component)
  ;  (defn my-component [component-id content-props])
  ;  [components/content {:content       #'my-component
  ;                       :content-props {...}}]
  ;
  ; @example (component)
  ;  (defn my-component [component-id content-props view-props])
  ;  [components/content {:content       #'my-component
  ;                       :content-props {...}
  ;                       :subscriber    [::get-view-props]}]
  ;
  ; @example (component)
  ;  (defn my-component [])
  ;  [components/content {:content [my-component]}]
  ;
  ; @example (component)
  ;  (defn my-component-a [])
  ;  (defn my-component-b [])
  ;  [components/content {:content [:<> [my-component-a]
  ;                                     [my-component-b]]}]
  ;
  ; @return (component or string)
  ([context-props]
   (component (a/id) context-props))

  ([component-id context-props]
   (if-not (map? context-props)
           ; TODO ...
           (content component-id {:content context-props})
           (content component-id context-props))))
