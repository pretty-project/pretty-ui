
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books.social-media)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:airbnb-link
           {:en "Airbnb link"
            :hu "Airbnb hivatkozás"}
           :airbnb-links
           {:en "Airbnb links"
            :hu "Airbnb hivatkozások"}
           :airbnb-link-placeholder
           {:en ""
            :hu ""}
           :awwwards-link
           {:en "Awwwards link"
            :hu "Awwwards hivatkozás"}
           :awwwards-links
           {:en "Awwwards links"
            :hu "Awwwards hivatkozások"}
           :awwwards-link-placeholder
           {:en ""
            :hu ""}
           :behance-link
           {:en "Behance link"
            :hu "Behance hivatkozás"}
           :behance-links
           {:en "Behance links"
            :hu "Behance hivatkozások"}
           :behance-link-placeholder
           {:en ""
            :hu ""}
           :facebook-link
           {:en "Facebook link"
            :hu "Facebook hivatkozás"}
           :facebook-links
           {:en "Facebook links"
            :hu "Facebook hivatkozások"}
           :facebook-link-placeholder
           {:en "facebook.com/profile"
            :hu "facebook.com/profile"}
           :fiverr-link
           {:en "Fiverr link"
            :hu "Fiverr hivatkozás"}
           :fiverr-links
           {:en "Fiverr links"
            :hu "Fiverr hivatkozások"}
           :fiverr-link-placeholder
           {:en ""
            :hu ""}
           :github-link
           {:en "GitHub link"
            :hu "GitHub hivatkozás"}
           :github-links
           {:en "GitHub links"
            :hu "GitHub hivatkozások"}
           :github-link-placeholder
           {:en "github.com/user"
            :hu "github.com/user"}
           :go-to-airbnb!
           {:en "Go to Airbnb"
            :hu "Tovább az Airbnb-re"}
           :go-to-awwwards!
           {:en "Go to Awwwards"
            :hu "Tovább az Awwwards-ra"}
           :go-to-behance!
           {:en "Go to Behance"
            :hu "Tovább a Behance-re"}
           :go-to-facebook!
           {:en "Go to Facebook"
            :hu "Tovább a Facebook-ra"}
           :go-to-fiverr!
           {:en "Go to Fiverr"
            :hu "Tovább a Fiverr-re"}
           :go-to-github!
           {:en "Go to GitHub"
            :hu "Tovább a GitHub-ra"}
           :go-to-instagram!
           {:en "Go to Instagram"
            :hu "Tovább az Instagram-ra"}
           :go-to-linkedin!
           {:en "Go to LinkedIn"
            :hu "Tovább a LinkedIn-re"}
           :go-to-pinterest!
           {:en "Go to Pinterest"
            :hu "Tovább a Pinterest-re"}
           :go-to-reddit!
           {:en "Go to Reddit"
            :hu "Tovább a Reddit-re"}
           :go-to-snapchat!
           {:en "Go to Snapchat"
            :hu "Tovább a Snapchat-re"}
           :go-to-skype!
           {:en "Go to Skype"
            :hu "Tovább a Skype-ra"}
           :go-to-tiktok!
           {:en "Go to TikTok"
            :hu "Tovább a TikTok-ra"}
           :go-to-twitter!
           {:en "Go to Twitter"
            :hu "Tovább a Twitter-re"}
           :go-to-viber!
           {:en "Go to Viber"
            :hu "Tovább a Viber-re"}
           :go-to-vimeo!
           {:en "Go to Vimeo"
            :hu "Tovább a Vimeo-ra"}
           :go-to-whatsapp!
           {:en "Go to WhatsApp"
            :hu "Tovább a WhatsApp-ra"}
           :go-to-youtube!
           {:en "Go to Youtube"
            :hu "Tovább a Youtube-ra"}
           :instagram-link
           {:en "Instagram link"
            :hu "Instagram hivatkozás"}
           :instagram-links
           {:en "Instagram links"
            :hu "Instagram hivatkozások"}
           :instagram-link-placeholder
           {:en "instagram.com/profile"
            :hu "instagram.com/profile"}
           :linkedin-link
           {:en "LinkedIn link"
            :hu "LinkedIn hivatkozás"}
           :linkedin-links
           {:en "LinkedIn links"
            :hu "LinkedIn hivatkozások"}
           :linkedin-link-placeholder
           {:en ""
            :hu ""}
           :pinterest-link
           {:en "Pinterest link"
            :hu "Pinterest hivatkozás"}
           :pinterest-links
           {:en "Pinterest links"
            :hu "Pinterest hivatkozások"}
           :pinterest-link-placeholder
           {:en ""
            :hu ""}
           :reddit-link
           {:en "Reddit link"
            :hu "Reddit hivatkozás"}
           :reddit-links
           {:en "Reddit links"
            :hu "Reddit hivatkozások"}
           :reddit-link-placeholder
           {:en ""
            :hu ""}
           :snapchat-link
           {:en "Snapchat link"
            :hu "Snapchat hivatkozás"}
           :snapchat-links
           {:en "Snapchat links"
            :hu "Snapchat hivatkozások"}
           :snapchat-link-placeholder
           {:en ""
            :hu ""}
           :social-media
           {:en "Social media"
            :hu "Közösségi média"}
           :social-media-link
           {:en "Social media link"
            :hu "Közösségi média hivatkozás"}
           :social-media-links
           {:en "Social media links"
            :hu "Közösségi média hivatkozások"}
           :skype-link
           {:en "Skype link"
            :hu "Skype hivatkozás"}
           :skype-links
           {:en "Skype links"
            :hu "Skype hivatkozások"}
           :skype-link-placeholder
           {:en ""
            :hu ""}
           :tiktok-link
           {:en "TikTok link"
            :hu "TikTok hivatkozás"}
           :tiktok-links
           {:en "TikTok links"
            :hu "TikTok hivatkozások"}
           :tiktok-link-placeholder
           {:en ""
            :hu ""}
           :twitter-link
           {:en "Twitter link"
            :hu "Twitter hivatkozás"}
           :twitter-links
           {:en "Twitter links"
            :hu "Twitter hivatkozások"}
           :twitter-link-placeholder
           {:en ""
            :hu ""}
           :viber-link
           {:en "Viber link"
            :hu "Viber hivatkozás"}
           :viber-links
           {:en "Viber links"
            :hu "Viber hivatkozások"}
           :viber-link-placeholder
           {:en ""
            :hu ""}
           :vimeo-link
           {:en "Vimeo link"
            :hu "Vimeo hivatkozás"}
           :vimeo-links
           {:en "Vimeo links"
            :hu "Vimeo hivatkozások"}
           :vimeo-link-placeholder
           {:en ""
            :hu ""}
           :whatsapp-link
           {:en "WhatsApp link"
            :hu "WhatsApp hivatkozás"}
           :whatsapp-links
           {:en "WhatsApp links"
            :hu "WhatsApp hivatkozások"}
           :whatsapp-link-placeholder
           {:en ""
            :hu ""}
           :youtube-link
           {:en "Youtube link"
            :hu "Youtube hivatkozás"}
           :youtube-links
           {:en "Youtube links"
            :hu "Youtube hivatkozások"}
           :youtube-link-placeholder
           {:en "youtube.com/channel/name"
            :hu "youtube.com/channel/name"}})
