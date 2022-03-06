
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.app-details      :as details]
              [x.server-core.api  :as a :refer [cache-control-uri]]
              [x.server-ui.engine :refer [include-css include-favicon include-font]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
;  [{:core-js (string)(opt)
;    :uri (string)}]
(def SYSTEM-CSS-PATHS [{:uri "/css/x/app-fonts.css"}
                       {:uri "/css/normalize.css"}
                       {:uri "/css/x/animations.css"}
                       {:uri "/css/x/app-ui-profiles.css"}
                       {:uri "/css/x/app-ui-themes.css"}
                       {:uri "/css/x/app-ui-structure.css"}
                       {:uri "/css/x/app-ui-animations.css"}
                       {:uri "/css/x/app-ui-graphics.css"}
                       {:uri "/css/x/app-layouts.css"}
                       {:uri "/css/x/app-elements.css"}
                       {:uri "/css/x/app-views.css"}
                       {:uri "/css/extensions.css"}
                       {:uri "/css/plugins.css"}
                       ; Using self hosted Font Awesome icons
                       {:uri "/icons/fontawesome-free-5.15.1-web/css/all.min.css"}
                       ; XXX#8857
                       ; Using Material Icons via Google Web Fonts
                       {:uri "https://fonts.googleapis.com/icon?family=Material+Icons"}
                       {:uri "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined"}])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn meta-keywords->formatted-meta-keywords
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

(defn head<-crawler-settings
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
       (vector/concat-items head [[:meta {:content crawler-rules    :name "robots"}]
                                  [:meta {:content meta-description :name "description"}]
                                  [:meta {:content meta-keywords    :name "keywords"}]])))

(defn head<-browser-settings
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
  (vector/concat-items head [[:title app-title]
                             [:meta {:charset "utf-8"}]
                             ; maximum-scale=1
                             ; A mobileszköz böngészők ne nagyítsák a tartalmat input elemek kitöltése közben.
                             ; https://stackoverflow.com/questions/2989263/disable-auto-zoom-in-input-text-tag-safari-on-iphone
                             [:meta {:content "width=320, initial-scale=1 maximum-scale=1" :name "viewport"}]
                             [:meta {:content theme-color                                  :name "theme-color"}]
                             [:meta {:content selected-language                            :http-equiv "content-language"}]]))

(defn head<-legal-information
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:author (string)}
  ;
  ; @return (hiccup)
  [head request {:keys [author]}]
  (vector/concat-items head [[:meta {:content author                        :name "author"}]
                             [:meta {:content details/copyright-information :name "copyright"}]
                             [:meta {:content details/app-version           :name "version"}]]))

(defn head<-og-properties
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
  (let [og-url (http/request->uri request)]
       (vector/concat-items head [[:meta {:content "website"        :property "og:type"}]
                                  [:meta {:content meta-description :property "og:description"}]
                                  [:meta {:content app-title        :property "og:title"}]
                                  [:meta {:content og-preview-path  :property "og:image"}]
                                  [:meta {:content og-url           :property "og:url"}]])))

(defn head<-css-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-build (string)(opt)
  ;   :core-js (string)
  ;   :css-paths (maps in vector)
  ;    [{:core-js (string)(opt)
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [head request {:keys [app-build core-js css-paths] :as head-props}]
  (letfn [(include-css? [css-props] (or (-> css-props :core-js nil?)
                                        (-> css-props :core-js (= core-js))))
          (f [head {:keys [uri] :as css-props}]
             (let [cache-control-uri (cache-control-uri uri app-build)
                   css-props         (assoc css-props :uri cache-control-uri)]
                  (if (include-css? css-props)
                      (conj   head (include-css css-props))
                      (return head))))]
         (let [css-paths (vector/concat-items css-paths SYSTEM-CSS-PATHS)]
              (reduce f head css-paths))))

(defn head<-favicon-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-build (string)(opt)
  ;   :core-js (string)
  ;   :favicon-paths (maps in vector)
  ;    [{:core-js (string)(opt)
  ;      :size (string)
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [head request {:keys [app-build core-js favicon-paths]}]
  (letfn [(include-favicon? [favicon-props] (or (-> favicon-props :core-js nil?)
                                                (-> favicon-props :core-js (= core-js))))
          (f [head {:keys [uri] :as favicon-props}]
             (let [cache-control-uri (cache-control-uri uri app-build)
                   favicon-props     (assoc favicon-props :uri cache-control-uri)]
                  (if (include-favicon? favicon-props)
                      (conj   head (include-favicon favicon-props))
                      (return head))))]
         (reduce f head favicon-paths)))
