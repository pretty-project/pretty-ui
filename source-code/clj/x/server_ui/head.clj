
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.09
; Description:
; Version: v1.2.0
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.app-details      :as details]
              [x.server-core.api  :as a :refer [cache-control-uri]]
              [x.server-user.api  :as user]
              [x.server-ui.engine :refer [include-css include-favicon include-font]]
              [x.server-environment.api :as environment]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def SYSTEM-CSS-PATHS [{:uri "/css/x/app-fonts.css"         :cache-control? true}
                       {:uri "/css/normalize.css"           :cache-control? true}
                       {:uri "/css/x/animations.css"        :cache-control? true}
                       {:uri "/css/x/app-ui-profiles.css"   :cache-control? true}
                       {:uri "/css/x/app-ui-themes.css"     :cache-control? true}
                       {:uri "/css/x/app-ui-structure.css"  :cache-control? true}
                       {:uri "/css/x/app-ui-animations.css" :cache-control? true}
                       {:uri "/css/x/app-ui-graphics.css"   :cache-control? true}
                       {:uri "/css/x/app-layouts.css"       :cache-control? true}
                       {:uri "/css/x/app-elements.css"      :cache-control? true}
                       {:uri "/css/x/app-views.css"         :cache-control? true}
                       {:uri "/css/extensions.css"          :cache-control? true}
                       {:uri "/css/plugins.css"             :cache-control? true}
                       ; Using self hosted Font Awesome icons
                       {:uri "/icons/fontawesome-free-5.15.1-web/css/all.min.css"}
                       ; XXX#8857
                       ; Using Material Icons via Google Web Fonts
                       {:uri "https://fonts.googleapis.com/icon?family=Material+Icons"}
                       {:uri "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined"}])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- meta-keywords->formatted-meta-keywords
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string or strings in vector) meta-keywords
  ;
  ; @example
  ;  (meta-keywords->formatted-meta-keywords ["My" "keywords"])
  ;  =>
  ;  "My, keywords"
  ;
  ; @example
  ;  (meta-keywords->formatted-meta-keywords "My, keywords")
  ;  =>
  ;  "My, keywords"
  ;
  ; @return (string)
  [meta-keywords]
  (if (string?     meta-keywords)
      (return      meta-keywords)
      (string/join meta-keywords ", ")))

(defn- head<-crawler-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:crawler-rules (string)
  ;   :meta-description (string)
  ;   :meta-keywords (string or strings in vector)}
  ;
  ; @return (hiccup)
  [head request {:keys [crawler-rules meta-description meta-keywords]}]
  (let [meta-keywords (meta-keywords->formatted-meta-keywords meta-keywords)]
       (vector/concat-items head [[:meta {:content (str crawler-rules)    :name "robots"}]
                                  [:meta {:content (str meta-description) :name "description"}]
                                  [:meta {:content (str meta-keywords)    :name "keywords"}]])))

(defn- head<-browser-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-title (string)
  ;   :selected-language (keyword)
  ;   :theme-color (string)}
  ;
  ; @return (hiccup)
  [head request {:keys [app-title selected-language theme-color]}]
  (vector/concat-items head [[:title (str app-title)]
                             [:meta {:charset "utf-8"}]
                             ; maximum-scale=1
                             ; A mobileszköz böngészők ne nagyítsák a tartalmat input elemek kitöltése közben.
                             ; https://stackoverflow.com/questions/2989263/disable-auto-zoom-in-input-text-tag-safari-on-iphone
                             [:meta {:content "width=320, initial-scale=1 maximum-scale=1" :name "viewport"}]
                             [:meta {:content theme-color                                  :name "theme-color"}]
                             [:meta {:content selected-language                            :http-equiv "content-language"}]]))

(defn- head<-legal-information
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:author (string)}
  ;
  ; @return (hiccup)
  [head request {:keys [author]}]
  (vector/concat-items head [[:meta {:content (str author)                        :name "author"}]
                             [:meta {:content (str details/copyright-information) :name "copyright"}]
                             [:meta {:content (str details/app-version)           :name "version"}]]))

(defn- head<-og-properties
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; The Open Graph protocol
  ; https://ogp.me/
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-title (string)
  ;   :meta-description (string)
  ;   :og-preview-path (string)}
  ;
  ; @return (hiccup)
  [head request {:keys [app-title meta-description og-preview-path]}]
  (vector/concat-items head [[:meta {:content "website"                   :property "og:type"}]
                             [:meta {:content (str meta-description)      :property "og:description"}]
                             [:meta {:content (str app-title)             :property "og:title"}]
                             [:meta {:content (str og-preview-path)       :property "og:image"}]
                             [:meta {:content (http/request->uri request) :property "og:url"}]]))

(defn- head<-css-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-build (string)(opt)
  ;   :css-paths (maps in vector)
  ;    [{:cache-control? (boolean)(opt)
  ;       Default: false
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [head request {:keys [app-build css-paths]}]
  (reduce (fn [head {:keys [cache-control? uri] :as css-props}]
              (if cache-control? (let [cache-control-uri (cache-control-uri uri app-build)
                                       css-props         (assoc css-props :uri cache-control-uri)]
                                      (conj head (include-css css-props)))
                                 (conj      head (include-css css-props))))
          (param head)
          (vector/concat-items css-paths SYSTEM-CSS-PATHS)))

(defn- head<-favicon-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-build (string)(opt)
  ;   :favicon-paths (maps in vector)
  ;    [{:cache-control? (boolean)(opt)
  ;       Default: false
  ;      :size (string)
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [head request {:keys [app-build favicon-paths]}]
  (reduce (fn [head {:keys [cache-control? uri] :as favicon-props}]
              (if cache-control? (let [cache-control-uri (cache-control-uri uri app-build)
                                       favicon-props     (assoc favicon-props :uri cache-control-uri)]
                                      (conj head (include-favicon favicon-props)))
                                 (conj      head (include-favicon favicon-props))))
          (param head)
          (param favicon-paths)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- head-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) head-props
  ;
  ; @return (map)
  ;  {:app-build (string)
  ;   :crawler-rules (?)
  ;   :selected-language (keyword)}
  [request head-props]
  (merge @(a/subscribe [:core/get-app-config])
          {:app-build         (a/app-build)
           :crawler-rules     (environment/crawler-rules request)
           :selected-language (user/request->user-settings-item request :selected-language)}
          (param head-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  ; @param (map)(opt) head-props
  ;  {:app-build (string)(opt)
  ;   :app-title (string)(opt)
  ;   :author (string)(opt)
  ;   :crawler-rules (string)(opt)
  ;   :css-paths (maps in vector)(opt)
  ;    [{:cache-control? (boolean)(opt)
  ;       Default: false
  ;      :uri (string)}]
  ;   :favicon-paths (maps in vector)(opt)
  ;    [{:cache-control? (boolean)(opt)
  ;       Default: false
  ;      :size (string)
  ;      :uri (string)}]
  ;   :meta-description (string)(opt)
  ;   :meta-keywords (string or strings in vector)(opt)
  ;   :og-preview-path (string)(opt)
  ;   :selected-language (keyword)(opt)
  ;   :theme-color (string)(opt)
  ;
  ; @usage
  ;  (ui/head {...} {...})
  ;
  ; @return (hiccup)
  ([request]
   (view request {}))

  ([request head-props]
   (let [head-props (head-props-prototype request head-props)]
        (-> [:head#x-head]
            (head<-legal-information request head-props)
            (head<-browser-settings  request head-props)
            (head<-crawler-settings  request head-props)
            (head<-og-properties     request head-props)
            (head<-css-includes      request head-props)
            (head<-favicon-includes  request head-props)))))
